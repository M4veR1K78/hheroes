package mav.com.hheroes.domain;

public class Cadeau {
	private Integer id;
	
	private String nom;
	
	private Integer affectation;
	
	private Integer prix;
	
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
}
