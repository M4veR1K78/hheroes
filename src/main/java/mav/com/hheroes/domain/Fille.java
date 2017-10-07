package mav.com.hheroes.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fille {
	public static final String LEVEL_UPGRADE = "En upgrade";
	public static final String LEVEL_MAX = "Max.";

	private Integer id;

	private String name;

	private Integer level;

	private Integer grade;

	private String expLeftNextLevel;

	private String affLeftNextLevel;

	private String cumulAff;

	private Double salaryPerHour;

	private String favoritePosition;

	private Double hardcore;

	private Double charme;

	private Double savoirFaire;

	private String type;

	private Integer typeId;

	private String avatar;

	private Double salary;

	private Boolean collectable;

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

	public String getExpLeftNextLevel() {
		return expLeftNextLevel;
	}

	public void setExpLeftNextLevel(String expLeftNextLevel) {
		this.expLeftNextLevel = expLeftNextLevel;
	}

	public String getAffLeftNextLevel() {
		return affLeftNextLevel;
	}

	public void setAffLeftNextLevel(String affLeftNextLevel) {
		this.affLeftNextLevel = affLeftNextLevel;
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
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getCumulAff() {
		return cumulAff;
	}

	public void setCumulAff(String cumulAff) {
		this.cumulAff = cumulAff;
	}

	public Double getAffProgress() {
		if (LEVEL_MAX.equals(cumulAff) || LEVEL_UPGRADE.equals(cumulAff)) {
			return 1.0;
		}

		Double cumul = getDoubleValue(cumulAff);
		return new BigDecimal(cumul / (cumul + getDoubleValue(affLeftNextLevel)))
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}

	private Double getDoubleValue(String valeur) {
		String valeurCleaned = valeur.replaceAll(" ", "");

		if (valeurCleaned.equals(LEVEL_MAX)) {
			return 9999.0;
		}
		if (valeurCleaned.equals(LEVEL_UPGRADE)) {
			return 0.0;
		}

		return Double.valueOf(valeurCleaned);
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
		return collectable;
	}

	public void setCollectable(Boolean canCollect) {
		this.collectable = canCollect;
	}
}
