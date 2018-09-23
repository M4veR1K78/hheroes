package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatUpdateResponseDTO {
	private int carac1;

	private int carac2;

	private int carac3;
	
	private int chance;

	private int endurance;

	@JsonProperty("next_price")
	private int nextPrice;

	@JsonProperty("soft_currency")
	private int softCurrency;

	private boolean success;

	public int getCarac1() {
		return carac1;
	}

	public void setCarac1(int carac1) {
		this.carac1 = carac1;
	}

	public int getCarac2() {
		return carac2;
	}

	public void setCarac2(int carac2) {
		this.carac2 = carac2;
	}

	public int getCarac3() {
		return carac3;
	}

	public void setCarac3(int carac3) {
		this.carac3 = carac3;
	}

	public int getEndurance() {
		return endurance;
	}

	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}

	public int getNextPrice() {
		return nextPrice;
	}

	public void setNextPrice(int nextPrice) {
		this.nextPrice = nextPrice;
	}

	public int getSoftCurrency() {
		return softCurrency;
	}

	public void setSoftCurrency(int softCurrency) {
		this.softCurrency = softCurrency;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}
}
