package mav.com.hheroes.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "hheroes")
public class Boss {
	@Id
	private Integer id;
	
	private Integer orgasm;
	
	private Double ego;
	
	private Double x;
	
	private Double d;
	
	private Integer nbOrg;
	
	private Integer figure;
	
	private String world;
	
	private String libelle;

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public String getLibelle() {
		return libelle;
	}
	
	public void setLibelle(String lib) {
		libelle = lib;
	}
}
