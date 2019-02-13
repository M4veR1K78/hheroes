package mav.com.hheroes.services.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO {
	private Boolean success;
	
	private EndDTO end;

	public EndDTO getEnd() {
		return end;
	}

	public void setEnd(EndDTO end) {
		this.end = end;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
