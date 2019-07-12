package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChampionDataDTO {
	private ChampionDTO champion;

	private TimerDTO timers;
	
	public boolean isActif() {
		return timers.getChampionHeal() != null;
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

	public void setTimers(TimerDTO timers) {
		this.timers = timers;
	}

	public static class TimerDTO {
		private Integer championRest;

		private Integer championHeal;

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
	}
}
