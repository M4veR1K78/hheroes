package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CadeauDTO {
	@JsonProperty("id_item")
	private Integer id;
	
	@JsonProperty("name")
	private String nom;
	
	@JsonProperty("value")
	private Integer affectation;
	
	@JsonProperty("price")
	private Integer prix;
	
	@JsonProperty("ico")
	private String urlImage;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getAffectation() {
		return affectation;
	}

	public void setAffectation(Integer affectation) {
		this.affectation = affectation;
	}

	public Integer getPrix() {
		return prix;
	}

	public void setPrix(Integer prix) {
		this.prix = prix;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Cadeau [id=" + id + ", nom=" + nom + ", affectation=" + affectation + ", prix=" + prix + ", urlImage="
				+ urlImage + "]";
	}
}
