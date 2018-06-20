package mav.com.hheroes.domain;

public enum Skill {
	HARDCORE(1, "Hardcore", "class1"), CHARME(2, "Charme", "class2"), SAVOIR_FAIRE(3, "Savoir-faire", "class3");

	private String type;
	private Integer code;
	private String classes;

	Skill(Integer code, String type, String classes)
	{
		this.code = code;
		this.type = type;
		this.classes = classes;
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
	
	public static Skill valueOfClass(String classes)
	{
		Skill[] values = Skill.values();

		for (Skill typeFille : values)
		{
			if (typeFille.classes.equals(classes))
			{
				return typeFille;
			}
		}

		return null;
	}
}
