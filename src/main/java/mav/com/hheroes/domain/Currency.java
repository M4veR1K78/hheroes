package mav.com.hheroes.domain;

public enum Currency {
	ENERGY("energy_quest"), 
	KOBAN("koban"),
	TICKET("ticket");
	
	private String type;
	
	Currency(String type) {
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
