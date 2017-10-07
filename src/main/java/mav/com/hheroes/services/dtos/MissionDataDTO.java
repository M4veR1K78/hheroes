package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionDataDTO {
	@JsonProperty("id_member_mission")
	private String idMemberMission;
	@JsonProperty("id_mission")
	private String idMission;
	@JsonProperty("cost")
	private String cost;

	public String getIdMemberMission() {
		return idMemberMission;
	}

	public void setIdMemberMission(String idMemberMission) {
		this.idMemberMission = idMemberMission;
	}

	public String getIdMission() {
		return idMission;
	}

	public void setIdMission(String idMission) {
		this.idMission = idMission;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

}
