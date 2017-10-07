package mav.com.hheroes.domain;

public enum TypeFille {
	HARDCORE(1, "Hardcore"), CHARME(2, "Charme"), SAVOIR_FAIRE(3, "Savoir-faire");

	private String type;
	private Integer code;

	TypeFille(Integer code, String type)
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

	public static TypeFille valueOf(Integer code)
	{
		TypeFille[] values = TypeFille.values();

		for (TypeFille typeFille : values)
		{
			if (typeFille.code.equals(code))
			{
				return typeFille;
			}
		}

		return null;
	}
}
