package mav.com.hheroes.services.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndDTO {
	private RewardDTO rewards;
		
	private boolean lose;

	public RewardDTO getRewards() {
		return rewards;
	}

	public void setRewards(RewardDTO reward) {
		this.rewards = reward;
	}

	@Override
	public String toString() {
		return rewards.toString();
	}

	public boolean isLose() {
		return lose;
	}

	public void setLose(boolean lose) {
		this.lose = lose;
	}
}
