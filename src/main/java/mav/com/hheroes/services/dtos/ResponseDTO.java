package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO {
	private Boolean success;
	
	@JsonProperty("end")
	private RewardDTO reward;

	public RewardDTO getReward() {
		return reward;
	}

	public void setReward(RewardDTO reward) {
		this.reward = reward;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
