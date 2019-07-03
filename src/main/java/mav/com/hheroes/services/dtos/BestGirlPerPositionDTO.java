package mav.com.hheroes.services.dtos;

import java.util.List;

public class BestGirlPerPositionDTO {
	private String position;
	
	private List<FilleLightDTO> filles;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<FilleLightDTO> getFilles() {
		return filles;
	}

	public void setFilles(List<FilleLightDTO> filles) {
		this.filles = filles;
	}
}
