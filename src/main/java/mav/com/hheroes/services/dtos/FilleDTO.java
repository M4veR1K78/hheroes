package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilleDTO {
	@JsonProperty("id_girl")
	private Integer idGirl;
	
	private Integer level;
	
	private Integer graded;
	
	@JsonProperty("Name")
	private String name;
	
	private Integer salary;
	
	@JsonProperty("pay_time")
	private Integer payTime;
	
	@JsonProperty("pay_in")
	private Integer payIn;

	public Integer getIdGirl() {
		return idGirl;
	}

	public void setIdGirl(Integer idGirl) {
		this.idGirl = idGirl;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getGraded() {
		return graded;
	}

	public void setGraded(Integer graded) {
		this.graded = graded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Integer getPayTime() {
		return payTime;
	}

	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}

	public Integer getPayIn() {
		return payIn;
	}

	public void setPayIn(Integer payIn) {
		this.payIn = payIn;
	}

	@Override
	public String toString() {
		return "FilleDTO [idGirl=" + idGirl + ", name=" + name + ", salary=" + salary + ", payTime=" + payTime + "]";
	}
}
