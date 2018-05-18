package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@Service
public class TaskExecutor {
	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * On a notre propre GameService ici pour ne pas qu'il interfere avec celui de l'application web
	 */
	private GameService gameService = new GameService();

	private FilleService filleService = new FilleService(gameService);

	private MissionService missionService = new MissionService(gameService);

	private ArenaService arenaService = new ArenaService(gameService);
	
	@Resource
	private BossService bossService;	
	
	@Resource
	private UserService userService;

	@Value("${hheroes.login}")
	private String login;

	@Value("${hheroes.password}")
	private String password;

	@Value("${hheroes.boss}")
	private Integer bossId;
	
	List<ScheduledExecutorService> threads = new ArrayList<>();

	/**
	 * Cette méthode de collecte des salaires des filles est appelée toutes les 20
	 * minutes.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	/*@Scheduled(cron = "${hheroes.cronCollectSalary}")
	public void collectSalary() throws IOException, AuthenticationException {
		if (gameService.getCookies(login) == null) {
			logger.info("Batch collectSalary login");
			gameService.login(login, password);
		}
		
		Double salaire = filleService.collectAllSalaries(login);

		logger.info(String.format("Batch collectSalary has been executed, %s $ collected", salaire));
	}*/
	
	@Scheduled(cron = "${hheroes.cronCollectSalary}")
	public void collectSalary() throws IOException, AuthenticationException {
		if (gameService.getCookies(login) == null) {
			logger.info("Batch collectSalary login");
			gameService.login(login, password);
		}
		
		filleService.getFilles(login).stream()
			.filter(Fille::isCollectable)
			.map(fille -> {
				logger.info(String.format("Starting thread for %s. Collect every %ss", fille.getName(), fille.getPayTime()));
				ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
				scheduler.scheduleAtFixedRate(new SalaryTask(filleService, fille.getId(), new UserDTO(login, password)), fille.getPayIn(), fille.getPayTime(), TimeUnit.SECONDS);
				return scheduler;
			}).forEach(threads::add);
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
		if (gameService.getCookies(login) == null) {
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
		if (gameService.getCookies(login) == null) {
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
		if (gameService.getCookies(login) == null) {
			logger.info("Batch doArene login");
			gameService.login(login, password);
		}

		List<JoueurDTO> joueurs = arenaService.getAllJoueurs(login);
		logger.info(String.format("Batch doArene Start (%s players)", joueurs.size()));
		for (JoueurDTO joueur : joueurs) {
			String log = String.format("\tPlayer arena %s attacked.", joueur.getArena());
			ResponseDTO response = arenaService.fight(joueur, login);
			if (response.getSuccess()) {
				log += String.format(" Results = %s", response.getReward().getWinner().equals(1) ? "Win" : "Loss");
			}
			logger.info(log);
		}
		if (joueurs.isEmpty()) {
			logger.info("\tNo fight to do...");
		}
	}
	
	@PreDestroy
	public void close() {
		threads.stream().forEach(ScheduledExecutorService::shutdownNow);
	}
}
