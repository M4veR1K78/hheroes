package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.domain.StatutMission;
import mav.com.hheroes.services.dtos.MissionDataDTO;

@Service
public class MissionService {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private GameService gameService;

	public MissionService(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Mission> getMissions(String login) throws IOException {
		Document activiteMission = gameService.getMissions(login);
		List<Mission> missions = new ArrayList<>();

		Elements select = activiteMission.select(".missions_wrap .mission_object");
		select.forEach(block -> {
			Mission mission = new Mission();
			mission.setTitre(block.select(".mission_details h1").text().trim());
			Elements exp = block.select(".slot_xp h3");
			mission.setExperience(Integer.valueOf(exp.isEmpty() ? "0" : cleanIntegerString(exp.text())));
			Elements buttons = block.select(".mission_button");
			mission.setDuree(Integer.valueOf(buttons.select(".duration").text()));

			// on cherche le statut de la mission
			if (!buttons.select("button[rel=\"claim\"]").is("[style*=display:none]")) {
				mission.setStatut(StatutMission.TERMINEE);
			} else if (!buttons.select(".finish_in_bar").is("[style*=display:none]")) {
				mission.setStatut(StatutMission.EN_COURS);
			} else if (buttons.select("button[rel=\"mission_start\"]").is("[disabled]")) {
				mission.setStatut(StatutMission.EN_ATTENTE);
			} else {
				mission.setStatut(StatutMission.PRETE);
			}
			try {
				MissionDataDTO data = new ObjectMapper().readValue(block.attr("data-d"), MissionDataDTO.class);
				mission.setId(data.getIdMission());
				mission.setIdMember(data.getIdMemberMission());
			} catch (IOException e) {
				logger.error("couldn't retrieve mission data", e);
			}

			missions.add(mission);
		});

		return missions;
	}

	public void acceptMission(Mission mission, String login) throws IOException {
		gameService.acceptMission(mission, login);
	}

	/**
	 * Faire toutes les missions. On peut choisir de logger les events ou non.
	 * 
	 * @param log
	 * @throws IOException
	 */
	public List<Mission> doAllMissions(String login, boolean log) throws IOException {
		UUID uuid = UUID.randomUUID();

		logger.info(String.format("Batch doMissions Start (id: %s)", uuid));

		AtomicInteger timer = new AtomicInteger(0);
		List<Mission> missions = getMissions(login);
		missions.stream()
				.filter(mission -> StatutMission.PRETE.equals(mission.getStatut()))
				.forEach(mission -> createThreadForMission(mission, timer, login));
		return missions;
	}
	
	private void createThreadForMission(Mission mission, AtomicInteger timer, String login) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(() -> {
			try {
				logger.info(String.format("Executing mission %s and sleeping %s secondes...", mission.getId(), mission.getDuree()));
				acceptMission(mission, login);
			} catch (IOException e) {
				logger.info(e);
			}
		}, timer.getAndAdd(mission.getDuree() + 1), TimeUnit.SECONDS);
	}

	/**
	 * Faire toutes les missions (pas de log)
	 * 
	 * @throws IOException
	 */
	@Async
	public void doAllMissions(String login) throws IOException {
		doAllMissions(login, false);
	}

	public void claimAllRewards(String login) throws IOException {
		getMissions(login).stream()
				.filter(mission -> StatutMission.TERMINEE.equals(mission.getStatut()))
				.forEach(mission -> {
					try {
						gameService.claimMissionReward(mission, login);
					} catch (IOException e) {
						logger.error(String.format("Fail claiming mission %s reward", mission.getId()));
					}
				});

		if (getMissions(login).isEmpty()) {
			// toutes les missions sont terminées et les récompenses ont été récupérés,
			// on récupère alors les kobans
			gameService.retrieveGift(login);
		}
	}

	/**
	 * Pour les string destiné à être transformé en double.
	 * 
	 * @param value
	 * @return
	 */
	private String cleanIntegerString(String value) {
		return value.replace("\u00a0", "").replace(",", "").replaceAll(" ", "");
	}
}
