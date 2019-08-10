package mav.com.hheroes.services.tasks;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.domain.Opponent;
import mav.com.hheroes.services.ArenaService;
import mav.com.hheroes.services.BossService;
import mav.com.hheroes.services.ChampionService;
import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.MissionService;
import mav.com.hheroes.services.TowerFameService;
import mav.com.hheroes.services.UserService;
import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.dtos.champion.ChampionDTO;
import mav.com.hheroes.services.dtos.champion.ChampionDataDTO;
import mav.com.hheroes.services.dtos.response.ResponseDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@Service
public class TaskExecutor {
	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * On a notre propre GameService ici pour ne pas qu'il interfere avec celui de
	 * l'application web
	 */
	private GameService gameService = new GameService();

	private FilleService filleService = new FilleService(gameService);

	private MissionService missionService = new MissionService(gameService);

	private ArenaService arenaService = new ArenaService(gameService);

	private TowerFameService towerService = new TowerFameService(gameService);
	
	private ChampionService championService = new ChampionService(gameService);

	@Autowired
	private BossService bossService;

	@Autowired
	private UserService userService;

	@Value("${hheroes.login}")
	private String login;

	@Value("${hheroes.password}")
	private String password;

	@Value("${hheroes.boss}")
	private Integer bossId;

	@Value("${hheroes.league}")
	private Integer league;
	
	@Value("${hheroes.championIds}")
	private String[] championIds;

	private Map<Integer, SchedulerInfo> threads = new HashMap<>();

	/**
	 * Collecte des salaires.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	@Scheduled(cron = "${hheroes.cronCollectSalary}")
	public void collectSalary() throws IOException, AuthenticationException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch collectSalary login");
			gameService.login(login, password);
		}

		List<Fille> filles = filleService.getFilles(login);
		filles.stream()
				.filter(fille -> !threads.containsKey(fille.getId()))
				.forEach(fille -> {
					logger.info(String.format("Starting thread for %s. Collect every %s", fille.getPseudo(),
							LocalTime.MIN.plusSeconds(fille.getPayTime()).toString()));

					ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
					ScheduledFuture<?> response = scheduler.scheduleWithFixedDelay(
							new PremiumAutoSalaryTask(filleService, fille.getId(), new UserDTO(login, password)),
							fille.getPayIn() + 1, fille.getPayTime(), TimeUnit.SECONDS);

					threads.put(fille.getId(), new SchedulerInfo(scheduler, fille, response));
				});

		// on regarde parmis les threads existants si une fille a monté en grade et donc
		// son timer a changé
		threads.entrySet().stream()
				.map(entry -> entry.getValue())
				.forEach(info -> {
					Fille fille = filles.get(filles.indexOf(info.getFille()));

					if (!fille.getPayTime().equals(info.getFille().getPayTime())) {
						// timer différent: on remplace la tâche existante par une nouvelle avec le
						// nouveau délai
						logger.info(String.format("Changing delay for %s", fille.getPseudo()));
						info.getResponse().cancel(false);
						info.setResponse(info.getScheduler().scheduleWithFixedDelay(
								new PremiumAutoSalaryTask(filleService, fille.getId(), new UserDTO(login, password)),
								fille.getPayIn() + 1, fille.getPayTime(), TimeUnit.SECONDS));
						info.setFille(fille);
					}
				});
	}

	/**
	 * Tous les jours à 7h15, cette méthode sera appelée de manière asynchrone afin
	 * d'accepter les différentes missions du jeu.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	@Async
	@Scheduled(cron = "${hheroes.cronDoMissions}")
	public void doMissions() throws IOException, AuthenticationException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doMissions login");
			gameService.login(login, password);
		}
		missionService.doAllMissions(login, true);
	}

	/**
	 * On fait le boss toutes les 2 heures.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 * @throws ObjectNotFoundException
	 */
	@Scheduled(cron = "${hheroes.cronDoBoss}")
	public void doBoss() throws IOException, AuthenticationException, ObjectNotFoundException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doBoss login");
			gameService.login(login, password);
		}
		bossService.setGameService(gameService);

		Integer id = userService.getByEmail(login)
				.map(user -> user.getBoss().getId())
				.orElse(bossId);

		logger.info(String.format("Batch doBoss Start (boss id = %s)", id));
		bossService.destroy(id, login, true);
	}

	/**
	 * On fait les combats d'arènes toutes les 30 minutes.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 * @throws ObjectNotFoundException
	 */
	@Scheduled(cron = "${hheroes.cronDoArena}")
	public void doArena() throws IOException, AuthenticationException, ObjectNotFoundException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doArene login");
			gameService.login(login, password);
		}

		List<JoueurDTO> joueurs = arenaService.getAllJoueurs(login);
		logger.info(String.format("Batch doArene Start (%s players)", joueurs.size()));
		for (JoueurDTO joueur : joueurs) {
			ResponseDTO response = arenaService.fight(joueur, login);
			if (!response.getSuccess()) {
				logger.info("\tErreur lors du combat d'arène. Success = false");
			}

		}
		if (joueurs.isEmpty()) {
			logger.info("\tNo fight to do...");
		}
	}

	@Scheduled(fixedDelayString = "${hheroes.cronDoPachinko}")
	public void doPachinko() throws AuthenticationException, IOException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doPachinko login");
			gameService.login(login, password);
		}

		logger.info("Doing pachinko...");
		gameService.playPachinko(login);
	}

	@Scheduled(cron = "${hheroes.cronDoLeagueBattle}")
	public void doLeagueBattle() throws AuthenticationException, IOException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doPachinko login");
			gameService.login(login, password);
		}

		logger.info("Doing league battles...");
		List<Opponent> opponents = towerService.getOpponents(login);
		boolean success = true;
		try {
			for (Opponent opponent : opponents) {
				while (success && opponent.getNbAttack() < 3) {
					logger.info("Preparing to attack " + opponent.getName() + " that has " + opponent.getNbAttack() + " attacks");
					Optional<JoueurDTO> joueur = arenaService.getJoueur(league, opponent.getId(), login);
					if (joueur.isPresent()) {
						ResponseDTO response = gameService.fightOpponent(joueur.get(), login);
						if (response.getSuccess()) {
							opponent.setNbAttack(opponent.getNbAttack() + 1);
						}
						success = response.getSuccess();
					} else {
						success = false;
					}
				}
				if (!success) {
					break;
				}
			}
		} catch (IOException e) {
			logger.error("Erreur lors du combat de league", e);
		}
	}
	
	@Scheduled(cron = "${hheroes.cronDoChampion}")
	public void doChampionBattle() throws AuthenticationException {
		if (!gameService.isConnected(login)) {
			logger.info("Batch doPachinko login");
			gameService.login(login, password);
		}
		
		championService.setChampionIds(championIds);
		
		championService.getAllChampions(login).stream()
			.filter(ChampionDataDTO::isActif)
			.filter(ChampionDataDTO::hasGirl)
			.map(ChampionDataDTO::getChampion)
			.sorted(Comparator.comparing(ChampionDTO::getId))
			.forEach(champion -> {
				try {
					logger.info("Fighting champion " + champion.getId() + ": " + champion.getName());
					championService.fightChampion(champion.getId(), Currency.TICKET, login);
				} catch (IOException e) {
					logger.error("An error occured while fighting the champion", e);
				}
			});
	}

	@PreDestroy
	public void close() {
		threads.entrySet().stream()
				.map(Entry::getValue)
				.map(SchedulerInfo::getScheduler)
				.forEach(ScheduledExecutorService::shutdownNow);
	}
}