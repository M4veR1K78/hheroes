package mav.com.hheroes.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Boss {
	public static final String DARK_LORD = "1";
	public static final String ESPION_NINJA = "2";
	
	@JsonProperty("id_troll")
	private String id;
		
	private Integer orgasm;
	
	private Double ego;
	
	private Double x;
	
	private Double d;
	
	@JsonProperty("nb_org")
	private Integer nbOrg;
	
	private Integer figure;
	
	@JsonProperty("id_world")
	private String world;

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrgasm() {
		return orgasm;
	}

	public void setOrgasm(Integer orgasm) {
		this.orgasm = orgasm;
	}

	public Double getEgo() {
		return ego;
	}

	public void setEgo(Double ego) {
		this.ego = ego;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public Integer getNbOrg() {
		return nbOrg;
	}

	public void setNbOrg(Integer nbOrg) {
		this.nbOrg = nbOrg;
	}

	public Integer getFigure() {
		return figure;
	}

	public void setFigure(Integer figure) {
		this.figure = figure;
	}
}
