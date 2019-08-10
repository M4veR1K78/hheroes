package mav.com.hheroes.services.dtos.champion.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import mav.com.hheroes.services.dtos.champion.GirlTeamDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DraftResponseDTO {
	private boolean canDraft;
	
	private Integer freeDrafts;
	
	private List<GirlTeamDTO> teamArray;

	public boolean isCanDraft() {
		return canDraft;
	}

	public void setCanDraft(boolean canDraft) {
		this.canDraft = canDraft;
	}

	public Integer getFreeDrafts() {
		return freeDrafts;
	}

	public void setFreeDrafts(Integer freeDrafts) {
		this.freeDrafts = freeDrafts;
	}

	public List<GirlTeamDTO> getTeamArray() {
		return teamArray;
	}

	public void setTeamArray(List<GirlTeamDTO> teamArray) {
		this.teamArray = teamArray;
	}
}
