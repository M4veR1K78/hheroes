package mav.com.hheroes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hero {
	private Integer id;

	private Integer xp;

	@JsonProperty("energy_quest")
	private Integer energyQuest;

	@JsonProperty("energy_fight")
	private Integer energyFight;

	@JsonProperty("soft_currency")
	private Integer money;

	@JsonProperty("hard_currency")
	private Integer kobans;

	private Integer level;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("energy_quest_max")
	private Integer eneryQuestMax;

	@JsonProperty("energy_fight_max")
	private Integer energyFightMax;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getXp() {
		return xp;
	}

	public void setXp(Integer xp) {
		this.xp = xp;
	}

	public Integer getEnergyQuest() {
		return energyQuest;
	}

	public void setEnergyQuest(Integer energyQuest) {
		this.energyQuest = energyQuest;
	}

	public Integer getEnergyFight() {
		return energyFight;
	}

	public void setEnergyFight(Integer energyFight) {
		this.energyFight = energyFight;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getKobans() {
		return kobans;
	}

	public void setKobans(Integer kobans) {
		this.kobans = kobans;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEneryQuestMax() {
		return eneryQuestMax;
	}

	public void setEneryQuestMax(Integer eneryQuestMax) {
		this.eneryQuestMax = eneryQuestMax;
	}

	public Integer getEnergyFightMax() {
		return energyFightMax;
	}

	public void setEnergyFightMax(Integer energyFightMax) {
		this.energyFightMax = energyFightMax;
	}

}
