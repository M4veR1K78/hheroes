package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndDTO {
	private Integer winner;

	private RewardDTO reward;
	
	private String drops;

	public Integer getWinner() {
		return winner;
	}

	public void setWinner(Integer winner) {
		this.winner = winner;
	}

	public RewardDTO getReward() {
		return reward;
	}

	public void setReward(RewardDTO reward) {
		this.reward = reward;
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
