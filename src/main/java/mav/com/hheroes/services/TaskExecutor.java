package mav.com.hheroes.services;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@Service
public class TaskExecutor {
	private final Logger logger = Logger.getLogger(getClass());

	private GameService gameService = new GameService();

	private FilleService filleService = new FilleService(gameService);

	private MissionService missionService = new MissionService(gameService);

	private BossService bossService = new BossService(gameService);
	
	private ArenaService arenaService = new ArenaService(gameService);

	@Value("${hheroes.login}")
	private String login;

	@Value("${hheroes.password}")
	private String password;

	@Value("${hheroes.boss}")
	private String bossId;
	
	@Value("${hheroes.cronCollectSalary}")
	private String cronCollectSalary;

	/**
	 * Cette méthode de collecte des salaires des filles est appelée toutes les 20
	 * minutes.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	@Scheduled(cron = "${hheroes.cronCollectSalary}")
	public void collectSalary() throws IOException, AuthenticationException {
		if (gameService.getCookies() == null) {
			logger.info("Batch collectSalary login");
			gameService.setCookies(gameService.login(login, password));
		}
		Double salaire = 0.0;

		List<Fille> filles = filleService.getFilles();
		for (Fille fille : filles) {
			if (fille.isCollectable()) {
				filleService.collectSalary(fille.getId());
				salaire += fille.getSalary();
			}
		}

		logger.info(String.format("Batch collectSalary has been executed, %s $ collected", salaire));
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
		if (gameService.getCookies() == null) {
			logger.info("Batch doMissions login");
			gameService.setCookies(gameService.login(login, password));
		}
		missionService.doAllMissions(true);
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
		if (gameService.getCookies() == null) {
			logger.info("Batch doBoss login");
			gameService.setCookies(gameService.login(login, password));
		}

		logger.info(String.format("Batch doBoss Start (boss id = %s)", bossId));
		bossService.destroy(bossId, true);
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
		if (gameService.getCookies() == null) {
			logger.info("Batch doArene login");
			gameService.setCookies(gameService.login(login, password));
		}

		logger.info("Batch doArene Start");
		List<JoueurDTO> joueurs = arenaService.getAllJoueurs();
		for (JoueurDTO joueur : joueurs) {
			String log = String.format("\tPlayer %s attacked.", joueur.getId());
			ResponseDTO response = arenaService.fight(joueur);
			if (response.getSuccess()) {
				log += String.format(" Results = %s", response.getReward().getWinner().equals(1) ? "Win" : "Loss");
			}
			logger.info(log);
		}
		if (joueurs.isEmpty()) {
			logger.info("\tNo fight to do...");
		}
	}

}
