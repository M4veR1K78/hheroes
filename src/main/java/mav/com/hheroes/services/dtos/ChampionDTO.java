package mav.com.hheroes.services.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChampionDTO {
	private Integer id;

	private String name;

	private String lairName;

	private Integer tier;

	private StageDTO stage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLairName() {
		return lairName;
	}

	public void setLairName(String lairName) {
		this.lairName = lairName;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public StageDTO getStage() {
		return stage;
	}

	public void setStage(StageDTO stage) {
		this.stage = stage;
	}

	public static class StageDTO {
		private Integer last;

		private Integer current;

		private Integer max;

		public Integer getLast() {
			return last;
		}

		public void setLast(Integer last) {
			this.last = last;
		}

		public Integer getCurrent() {
			return current;
		}

		public void setCurrent(Integer current) {
			this.current = current;
		}

		public Integer getMax() {
			return max;
		}

		public void setMax(Integer max) {
			this.max = max;
		}

	}
}
