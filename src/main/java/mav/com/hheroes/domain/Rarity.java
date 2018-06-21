package mav.com.hheroes.domain;

public enum Rarity {
	DEPART("starting", "Fille De Départ"), 
	COMMUN("common", "Commune"), 
	RARE("rare", "Rare"), 
	EPIQUE("epic", "Épique"), 
	LEGENDAIRE("legendary", "Légendaire");
	
	private String libelle;
	
	private String type;
	
	Rarity(String type, String libelle) {
		this.type = type;
		this.setLibelle(libelle);
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public static Rarity valueOfType(String type) {
		Rarity[] values = Rarity.values();

		for (Rarity rarete : values)
		{
			if (rarete.type.equals(type))
			{
				return rarete;
			}
		}

		return null;
	}
}
