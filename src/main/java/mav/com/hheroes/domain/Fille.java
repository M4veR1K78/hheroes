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

	private Double expertiseBaseValue;

	private Integer expertiseRanking;

	private Integer payTime;

	private Integer payIn;

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
		if (cumulAff.isEmpty()) {
			return null;
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

}
