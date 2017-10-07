package mav.com.hheroes.domain;

public enum Skill {
	HARDCORE(1, "Hardcore"), CHARME(2, "Charme"), SAVOIR_FAIRE(3, "Savoir-faire");

	private String type;
	private Integer code;

	Skill(Integer code, String type)
	{
		this.code = code;
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
	
	public Integer getCode() {
		return code;
	}

	@Override
	public String toString()
	{
		return getType();
	}

	public static Skill valueOf(Integer code)
	{
		Skill[] values = Skill.values();

		for (Skill typeFille : values)
		{
			if (typeFille.code.equals(code))
			{
				return typeFille;
			}
		}

		return null;
	}
}
