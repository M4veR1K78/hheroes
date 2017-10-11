package mav.com.hheroes.services;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.exceptions.AuthenticationException;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@Service
public class TaskExecutor {
	private final Logger logger = Logger.getLogger(getClass());

	private GameService gameService = new GameService();

	private FilleService filleService = new FilleService(gameService);

	private MissionService missionService = new MissionService(gameService);

	private BossService bossService = new BossService(gameService);

	@Value("${hheroes.login}")
	private String login;

	@Value("${hheroes.password}")
	private String password;

	@Value("${hheroes.boss}")
	private String bossId;

	/**
	 * Cette méthode de collecte des salaires des filles est appelée toutes les 20
	 * minutes.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	@Scheduled(cron = "0 */20 * * * *")
	public void collectSalary() throws IOException, AuthenticationException {
		if (gameService.getCookie() == null) {
			logger.info("Batch collectSalary login");
			gameService.setCookie(gameService.login(login, password));
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
	@Scheduled(cron = "0 15 06 * * *")
	public void doMissions() throws IOException, AuthenticationException {
		if (gameService.getCookie() == null) {
			logger.info("Batch doMissions login");
			gameService.setCookie(gameService.login(login, password));
		}
		missionService.doAllMissions(true);
	}

	/**
	 * On fait le boss toutes heures.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 * @throws ObjectNotFoundException
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void doBoss() throws IOException, AuthenticationException, ObjectNotFoundException {
		if (gameService.getCookie() == null) {
			logger.info("Batch doBoss login");
			gameService.setCookie(gameService.login(login, password));
		}

		logger.info(String.format("Batch doBoss Start (boss id = %s)", bossId));
		bossService.destroy(bossId, true);
	}

}
