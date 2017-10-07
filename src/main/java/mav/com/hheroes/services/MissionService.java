package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<Mission> getMissions() throws IOException
	{
		Document activiteMission = gameService.getMissions();
		List<Mission> missions = new ArrayList<>();
		
		Elements select = activiteMission.select(".missions_wrap .mission_object");
		select.forEach(block -> {
			Mission mission = new Mission();
			Elements exp = block.select(".slot_xp h3");
			mission.setExperience(Integer.valueOf(exp.isEmpty() ? "0" : exp.text()));
			Elements buttons = block.select(".mission_button");
			mission.setDuree(Integer.valueOf(buttons.select(".duration").text()));
			
			// on cherche le statut de la mission
			if (!buttons.select("button[rel=\"claim\"]").is("[style*=display:none]")) {
				mission.setStatut(StatutMission.TERMINEE);
			}
			else if (!buttons.select(".finish_in_bar").is("[style*=display:none]")) {
				mission.setStatut(StatutMission.EN_COURS);
			}
			else if (buttons.select("button[rel=\"mission_start\"]").is("[disabled]")) {
				mission.setStatut(StatutMission.EN_ATTENTE);
			}
			else {
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
}
