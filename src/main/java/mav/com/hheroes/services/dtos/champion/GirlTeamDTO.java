package mav.com.hheroes.services.dtos.champion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GirlTeamDTO {
	@JsonProperty("id_girl")
	private Integer idGirl;
	
	@JsonProperty("class")
	private Integer typeId;
	
	private int damage;

	public Integer getIdGirl() {
		return idGirl;
	}

	public void setIdGirl(Integer idGirl) {
		this.idGirl = idGirl;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
