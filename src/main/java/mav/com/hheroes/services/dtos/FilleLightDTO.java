package mav.com.hheroes.services.dtos;

import mav.com.hheroes.domain.Rarity;

public class FilleLightDTO {
	private String pseudo;
	
	private String avatar;
	
	private Integer level;
	
	private Integer typeId;
	
	private Rarity rarity;
	
	private String type;
	
	private Double expertiseBaseValue;
	
	private Integer expertiseRanking;

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String name) {
		this.pseudo = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getExpertiseBaseValue() {
		return expertiseBaseValue;
	}

	public void setExpertiseBaseValue(Double expertiseBaseValue) {
		this.expertiseBaseValue = expertiseBaseValue;
	}

	public Integer getExpertiseRanking() {
		return expertiseRanking;
	}

	public void setExpertiseRanking(Integer expertiseRanking) {
		this.expertiseRanking = expertiseRanking;
	}	
}
