package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardDTO {
	private Integer winner;
	
	private String drops;

	public Integer getWinner() {
		return winner;
	}

	public void setWinner(Integer winner) {
		this.winner = winner;
	}

	public String getDrops() {
		return drops;
	}

	public void setDrops(String drops) {
		this.drops = drops;
	}
	
	@Override
	public String toString() {
		return drops;
	}
}
