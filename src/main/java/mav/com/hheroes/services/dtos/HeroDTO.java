package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroDTO {
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
	
	@JsonProperty("energy_challenge")
	private Integer energyChallenge;
	
	@JsonProperty("class")
	private Integer classe;
	
	@JsonProperty("Xp")
	private XpDTO experience;

	public Integer getClasse() {
		return classe;
	}

	public void setClasse(Integer classe) {
		this.classe = classe;
	}

	public XpDTO getExperience() {
		return experience;
	}

	public void setExperience(XpDTO experience) {
		this.experience = experience;
	}

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
	
	public Integer getEnergyChallenge() {
		return energyChallenge;
	}

	public void setEnergyChallenge(Integer energyChallenge) {
		this.energyChallenge = energyChallenge;
	}

	public class XpDTO {
		private Integer cur;
		
		private Integer min;
		
		private Integer max;
		
		private Integer level;
		
		@JsonProperty("next_max")
		private Integer nextMax;
		
		private Integer left;
		
		private Double ratio;

		public Integer getCur() {
			return cur;
		}

		public void setCur(Integer cur) {
			this.cur = cur;
		}

		public Integer getMin() {
			return min;
		}

		public void setMin(Integer min) {
			this.min = min;
		}

		public Integer getMax() {
			return max;
		}

		public void setMax(Integer max) {
			this.max = max;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getNextMax() {
			return nextMax;
		}

		public void setNextMax(Integer nextMax) {
			this.nextMax = nextMax;
		}

		public Integer getLeft() {
			return left;
		}

		public void setLeft(Integer left) {
			this.left = left;
		}

		public Double getRatio() {
			return ratio;
		}

		public void setRatio(Double ratio) {
			this.ratio = ratio;
		}
	}
}
