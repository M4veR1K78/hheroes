package mav.com.hheroes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Fille {
	public static final String LEVEL_UPGRADE = "En upgrade";
	public static final String LEVEL_MAX = "Max.";

	private Integer id;

	private String name;
	
	private String pseudo;

	private Integer level;

	private Integer grade;

	private Integer expLeftNextLevel;

	private Integer affLeftNextLevel;

	private Double salaryPerHour;

	private String favoritePosition;

	private Double hardcore;

	private Double charme;

	private Double savoirFaire;

	private Integer typeId;

	private String avatar;

	private Double salary;

	private Double expertiseBaseValue;

	private Integer expertiseRanking;

	private Integer payTime;

	private Integer payIn;
	
	private Rarity rarity;
	
	private Integer currentAff;
	
	private Integer maxAff;
	
	public boolean upgradable;
	
	public boolean maxed;

	public boolean isUpgradable() {
		return upgradable;
	}

	public void setUpgradable(boolean upgradable) {
		this.upgradable = upgradable;
	}

	public boolean isMaxed() {
		return maxed;
	}

	public void setMaxed(boolean maxed) {
		this.maxed = maxed;
	}

	public void setExpLeftNextLevel(Integer expLeftNextLevel) {
		this.expLeftNextLevel = expLeftNextLevel;
	}

	public void setAffLeftNextLevel(Integer affLeftNextLevel) {
		this.affLeftNextLevel = affLeftNextLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public int getExpLeftNextLevel() {
		return expLeftNextLevel;
	}

	public int getAffLeftNextLevel() {
		return affLeftNextLevel;
	}

	public Double getSalaryPerHour() {
		return salaryPerHour;
	}

	public void setSalaryPerHour(Double salaryPerHour) {
		this.salaryPerHour = salaryPerHour;
	}

	public String getFavoritePosition() {
		return favoritePosition;
	}

	public void setFavoritePosition(String favoritePosition) {
		this.favoritePosition = favoritePosition;
	}

	public Double getHardcore() {
		return hardcore;
	}

	public void setHardcore(Double hardcore) {
		this.hardcore = hardcore;
	}

	public Double getCharme() {
		return charme;
	}

	public void setCharme(Double charme) {
		this.charme = charme;
	}

	public Double getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(Double savoirFaire) {
		this.savoirFaire = savoirFaire;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getType() {
		return Skill.valueOf(typeId).getType();
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Double getAffProgress() {
		if (currentAff == null) {
			return null;
		}
		if (maxAff.equals(currentAff)) {
			return 1.0;
		}

		return new BigDecimal(currentAff / maxAff)
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	/**
	 * Indique si on peut collecter le salaire de la fille.
	 * 
	 * @return
	 */
	public Boolean isCollectable() {
		return payIn == 0;
	}


	public Integer getExpertiseRanking() {
		return expertiseRanking;
	}

	public void setExpertiseRanking(Integer expertiseRanking) {
		this.expertiseRanking = expertiseRanking;
	}

	public Double getExpertiseBaseValue() {
		return expertiseBaseValue;
	}

	public void setExpertiseBaseValue(Double expertiseBaseValue) {
		this.expertiseBaseValue = expertiseBaseValue;
	}

	public Integer getPayTime() {
		return payTime;
	}

	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}

	public Integer getPayIn() {
		return payIn;
	}

	public void setPayIn(Integer payIn) {
		this.payIn = payIn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fille other = (Fille) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public Integer getCurrentAff() {
		return currentAff;
	}

	public void setCurrentAff(Integer currentAff) {
		this.currentAff = currentAff;
	}

	public Integer getMaxAff() {
		return maxAff;
	}

	public void setMaxAff(Integer maxAff) {
		this.maxAff = maxAff;
	}

}
