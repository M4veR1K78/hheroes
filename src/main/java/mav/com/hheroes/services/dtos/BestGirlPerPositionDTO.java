package mav.com.hheroes.services.dtos;

import java.util.List;

import mav.com.hheroes.domain.FavoritePosition;

public class BestGirlPerPositionDTO {
	private FavoritePosition position;
	
	private List<FilleLightDTO> filles;

	public FavoritePosition getPosition() {
		return position;
	}

	public void setPosition(FavoritePosition position) {
		this.position = position;
	}

	public List<FilleLightDTO> getFilles() {
		return filles;
	}

	public void setFilles(List<FilleLightDTO> filles) {
		this.filles = filles;
	}
}
