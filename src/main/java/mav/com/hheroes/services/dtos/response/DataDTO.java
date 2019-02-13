package mav.com.hheroes.services.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO {
	public List<RewardLightDTO> rewards;

	public List<RewardLightDTO> getRewards() {
		return rewards;
	}

	public void setRewards(List<RewardLightDTO> rewards) {
		this.rewards = rewards;
	}
	
	@Override
	public String toString() {
		String s = "";
		if (rewards != null) {
			s = rewards.stream().map(RewardLightDTO::getValue).collect(Collectors.joining("\n"));
		}
		return s;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RewardLightDTO {
		private String type;
		
		private String value;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		} 
	}
}
