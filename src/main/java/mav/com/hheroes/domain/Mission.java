package mav.com.hheroes.domain;

public class Mission {
	private String id;
	private String titre;
	private String idMember;
	private Integer experience;
	
	/**
	 * En seconde.
	 */
	private Integer duree;
	
	private StatutMission statut;

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getDuree() {
		return duree;
	}

	public void setDuree(Integer duree) {
		this.duree = duree;
	}

	public Boolean isFinished() {
		return StatutMission.TERMINEE.equals(statut);
	}

	public StatutMission getStatut() {
		return statut;
	}

	public void setStatut(StatutMission statut) {
		this.statut = statut;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdMember() {
		return idMember;
	}

	public void setIdMember(String idMember) {
		this.idMember = idMember;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}
