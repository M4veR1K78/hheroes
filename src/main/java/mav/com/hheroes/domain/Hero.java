package mav.com.hheroes.domain;

public class Hero {
	private Integer id;

	private Integer xp;

	private Integer energyQuest;

	private Integer energyFight;

	private Integer money;

	private Integer kobans;

	private Integer level;

	private String name;

	private Integer energyQuestMax;

	private Integer energyFightMax;
	
	private String classe;
	
	private Experience experience;
	
	private String avatarUrl;

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

	public Integer getEnergyQuestMax() {
		return energyQuestMax;
	}

	public void setEnergyQuestMax(Integer eneryQuestMax) {
		this.energyQuestMax = eneryQuestMax;
	}

	public Integer getEnergyFightMax() {
		return energyFightMax;
	}

	public void setEnergyFightMax(Integer energyFightMax) {
		this.energyFightMax = energyFightMax;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public Experience getExperience() {
		return experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
