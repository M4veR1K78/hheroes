package mav.com.hheroes.services.dtos;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChampionDataDTO {
	private ChampionDTO champion;

	private TimerDTO timers;
	
	public boolean isActif() {
		return timers.getChampionRest() == null && timers.getTeamRest() == null;
	}

	public ChampionDTO getChampion() {
		return champion;
	}

	public void setChampion(ChampionDTO champion) {
		this.champion = champion;
	}

	public TimerDTO getTimers() {
		return timers;
	}

	public void setTimers(Object timers) {
		if (timers instanceof ArrayList) {
			this.timers = new TimerDTO();
		} else {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) timers;
			this.timers = new TimerDTO(map.get("championRest"), map.get("championHeal"), map.get("teamRest"));
		}
	}

	public static class TimerDTO {
		private Integer championRest;

		private Integer championHeal;
		
		private Integer teamRest;
		
		public TimerDTO() {
			super();
		}
		
		public TimerDTO(Object championRest, Object championHeal, Object teamRest) {
			this.championHeal = championHeal != null ? Integer.valueOf(championHeal.toString()) : null;
			this.championRest = championRest != null ? Integer.valueOf(championRest.toString()) : null;
			this.teamRest = teamRest != null ? Integer.valueOf(teamRest.toString()) : null;
		}

		public Integer getChampionRest() {
			return championRest;
		}

		public void setChampionRest(Integer championRest) {
			this.championRest = championRest;
		}

		public Integer getChampionHeal() {
			return championHeal;
		}

		public void setChampionHeal(Integer championHeal) {
			this.championHeal = championHeal;
		}

		public Integer getTeamRest() {
			return teamRest;
		}

		public void setTeamRest(Integer teamRest) {
			this.teamRest = teamRest;
		}
	}
}
