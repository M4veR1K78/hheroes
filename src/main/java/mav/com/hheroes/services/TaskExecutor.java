package mav.com.hheroes.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.domain.StatutMission;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

@Service
public class TaskExecutor {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FilleService filleService;

	@Autowired
	private GameService gameService;

	@Autowired
	private MissionService missionService;

	@Autowired
	private BossService bossService;

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
	 * Tous les jours à 9h15, cette méthode sera appelée de manière asynchrone afin
	 * d'accepter les différentes missions du jeu.
	 * 
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	@Async
	@Scheduled(cron = "0 15 09 * * *")
	// @Scheduled(cron = "*/20 * * * * *")
	public void doMissions() throws IOException, AuthenticationException {
		if (gameService.getCookie() == null) {
			logger.info("Batch doMissions login");
			gameService.setCookie(gameService.login(login, password));
		}

		logger.info("Batch doMissions Start");
		List<Mission> missions = missionService.getMissions();
		while (!missions.isEmpty() && !allFinished(missions)) {
			Optional<Mission> findAny = missions.stream()
					.filter(mission -> StatutMission.PRETE.equals(mission.getStatut()))
					.findAny();

			if (findAny.isPresent()) {
				Mission mission = findAny.get();
				missionService.acceptMission(mission);
				logger.info(String.format("Executing mission %s and sleeping %s secondes...", mission.getId(),
						mission.getDuree()));
				try {
					Thread.sleep(mission.getDuree() * 1000L + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					logger.info(String.format("No mission available for now, sleeping 10 minutes..."));
					// on dort 10 minutes
					Thread.sleep(10 * 1000L * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			missions = missionService.getMissions();
		}

		logger.info("Batch doMissions end");
	}

	private boolean allFinished(List<Mission> missions) {
		return missions.stream().allMatch(mission -> StatutMission.TERMINEE.equals(mission.getStatut()));
	}

	/**
	 * On fait le boss toutes les 20 minutes (vu que la recharge d'énergie de combat est de 10 minutes)
	 */
	@Scheduled(cron = "0 0/20 * * * *")
	public void doBoss() throws IOException, AuthenticationException {
		if (gameService.getCookie() == null) {
			logger.info("Batch doBoss login");
			gameService.setCookie(gameService.login(login, password));
		}

		logger.info(String.format("Batch doBoss Start (bossId: %s)", bossId));
		Boss boss = bossService.getBoss(bossId);

		ResponseDTO response = bossService.fight(boss);
		while (response.getSuccess()) {
			response = bossService.fight(boss);
		}
		
		logger.info("Batch doBoss End");
	}

}
