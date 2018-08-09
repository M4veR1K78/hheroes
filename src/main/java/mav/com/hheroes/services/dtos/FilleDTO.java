package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilleDTO {
	@JsonProperty("id_girl")
	private Integer idGirl;
	
	private Integer level;
	
	private Integer graded;
	
	@JsonProperty("Name")
	private String name;
	
	private Double salary;
	
	@JsonProperty("salary_per_hour")
	private Double salaryPerHour;
	
	@JsonProperty("pay_time")
	private Integer payTime;
	
	@JsonProperty("pay_in")
	private Integer payIn;
	
	private Boolean own;
	
	private RefDTO ref;
	
	private CaracDTO caracs;
	
	@JsonProperty("Affection")
	private StatDTO affection;
	
	@JsonProperty("Xp")
	private StatDTO xp;
	
	@JsonProperty("class")
	private Integer typeId;
	
	@JsonProperty("ico")
	private String icone;
	
	private String avatar;
	
	@JsonProperty("position_img")
	private String position;
	
	@JsonProperty("can_upgrade")
	private boolean upgradable;
	
	private String rarity;

	public boolean isUpgradable() {
		return upgradable;
	}

	public void setUpgradable(boolean upgradable) {
		this.upgradable = upgradable;
	}

	public Integer getIdGirl() {
		return idGirl;
	}

	public void setIdGirl(Integer idGirl) {
		this.idGirl = idGirl;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getGraded() {
		return graded;
	}

	public void setGraded(Integer graded) {
		this.graded = graded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
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
	public String toString() {
		return "FilleDTO [idGirl=" + idGirl + ", name=" + name + ", salary=" + salary + ", payTime=" + payTime + "]";
	}

	public Boolean isOwn() {
		return own;
	}

	public void setOwn(Boolean own) {
		this.own = own;
	}
	
	public RefDTO getRef() {
		return ref;
	}

	public void setRef(RefDTO ref) {
		this.ref = ref;
	}

	public Double getSalaryPerHour() {
		return salaryPerHour;
	}

	public void setSalaryPerHour(Double salaryPerHour) {
		this.salaryPerHour = salaryPerHour;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Boolean getOwn() {
		return own;
	}

	public CaracDTO getCaracs() {
		return caracs;
	}

	public void setCaracs(CaracDTO caracs) {
		this.caracs = caracs;
	}

	public StatDTO getAffection() {
		return affection;
	}

	public void setAffection(StatDTO affection) {
		this.affection = affection;
	}

	public StatDTO getXp() {
		return xp;
	}

	public void setXp(StatDTO xp) {
		this.xp = xp;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RefDTO {
		@JsonProperty("full_name")
		private String fullName;
		
		@JsonProperty("description")
		private String description;

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}		
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CaracDTO {
		@JsonProperty("carac1")
		private Double hardcore;
		
		@JsonProperty("carac2")
		private Double charme;
		
		@JsonProperty("carac3")
		private Double savoirFaire;

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
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StatDTO {
		private int left;
		
		@JsonProperty("cur")
		private int current;
		
		private int max;
		
		private boolean maxed;

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getCurrent() {
			return current;
		}

		public void setCurrent(int current) {
			this.current = current;
		}

		public boolean isMaxed() {
			return maxed;
		}

		public void setMaxed(boolean maxed) {
			this.maxed = maxed;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}
}
