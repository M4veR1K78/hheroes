package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	public List<Mission> getMissions() throws IOException {
		Document activiteMission = gameService.getMissions();
		List<Mission> missions = new ArrayList<>();

		Elements select = activiteMission.select(".missions_wrap .mission_object");
		select.forEach(block -> {
			Mission mission = new Mission();
			mission.setTitre(block.select(".mission_details h1").text().trim());
			Elements exp = block.select(".slot_xp h3");
			mission.setExperience(Integer.valueOf(exp.isEmpty() ? "0" : exp.text()));
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

	public void acceptMission(Mission mission) throws IOException {
		gameService.acceptMission(mission);
	}

	/**
	 * Faire toutes les missions. On peut choisir de logger les events ou non.
	 * 
	 * @param log
	 * @throws IOException
	 */
	@Async
	public void doAllMissions(boolean log) throws IOException {
		UUID uuid = UUID.randomUUID();

		logger.info(String.format("Batch doMissions Start (id: %s)", uuid));

		List<Mission> missions = getMissions();
		while (!missions.isEmpty() && !allFinished(missions)) {
			Optional<Mission> findAny = missions.stream()
					.filter(mission -> StatutMission.PRETE.equals(mission.getStatut()))
					.findAny();

			if (findAny.isPresent()) {
				Mission mission = findAny.get();
				acceptMission(mission);
				if (log) {
					logger.info(String.format("Executing mission %s and sleeping %s secondes...", mission.getId(),
							mission.getDuree()));
				}
				try {
					Thread.sleep(mission.getDuree() * 1000L + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					if (log) {
						logger.info(String.format("No mission available for now, sleeping 10 minutes..."));
					}
					// on dort 10 minutes
					Thread.sleep(10 * 1000L * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			missions = getMissions();
		}

		logger.info(String.format("Batch doMissions end  (id: %s)", uuid));
	}

	/**
	 * Faire toutes les missions (pas de log)
	 * 
	 * @throws IOException
	 */
	@Async
	public void doAllMissions() throws IOException {
		doAllMissions(false);
	}

	private boolean allFinished(List<Mission> missions) {
		return missions.stream().allMatch(mission -> StatutMission.TERMINEE.equals(mission.getStatut()));
	}
}
