package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoueurDTO {
	@JsonProperty("id_member")
	private String id;
		
	private Integer orgasm;
	
	private Double ego;
	
	private Double x;
	
	private Double d;
	
	@JsonProperty("nb_org")
	private Integer nbOrg;
	
	private Integer figure;
	
	@JsonProperty("id_arena")
	private String arena;

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

	public String getArena() {
		return arena;
	}

	public void setArena(String world) {
		this.arena = world;
	}
}
