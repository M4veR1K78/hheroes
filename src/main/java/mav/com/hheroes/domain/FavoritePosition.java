package mav.com.hheroes.domain;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FavoritePosition {
	SOIXANTE_NEUF("69", "69.png"), 
	LEVRETTE("Levrette", "doggystyle.png"), 
	PETIT_PONT("Petit pont", "bridge.png"),
	BROUETTE_THAILANDAISE("Brouette thaïlandaise", "indian.png"), 
	MISSIONAIRE("Missionnaire", "missionary.png"), 
	PILON("Pilon", "splittingbamboo.png"),
	CHAISE_LONGUE("Chaise longue", "jackhammer.png"), 
	NEOPHYTE("Néophyte", "dolphin.png"), 
	TENDRE_AMANT("Tendre amant", "nosedive.png"),
	MYSTERIEUSE_ENTREVUE("Mystérieuse entrevue", "column.png"), 
	SODOMIE("Sodomie", "sodomy.png"),
	UNION_SUSPENDUE("Union suspendue", "suspendedcongress.png");

	private String nom;

	private String image;

	FavoritePosition(String nom, String image) {
		this.nom = nom;
		this.image = image;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static FavoritePosition ofImage(String image) {
		return EnumSet.allOf(FavoritePosition.class).stream().filter(position -> position.getImage().equals(image))
				.findFirst().orElse(null);
	}
}
