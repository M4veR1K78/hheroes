package mav.com.hheroes.domain;

public class Opponent {
	private Long id;
	
	private Integer rank;
	
	private String name;
	
	private Integer level;
	
	private Integer nbAttack;
	
	private Integer points;

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

	public Integer getNbAttack() {
		return nbAttack;
	}

	public void setNbAttack(Integer nbAttack) {
		this.nbAttack = nbAttack;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
