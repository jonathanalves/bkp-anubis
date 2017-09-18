package anubis.enumeration;

public enum EnumStatusRetorno {

	ERROR(0, "error"), 
	SUCCESS(1, "success"),
	UPDATE(2, "update");

	private int valueInt;
	private String valueString;

	private EnumStatusRetorno(int valueInt, String valueString) {
		this.valueInt = valueInt;
		this.valueString = valueString;
	}

	public int getValueInt() {
		return valueInt;
	}
	
	public String getValueString() {
		return valueString;
	}

	public EnumStatusRetorno getEnumStatusRetorno(Integer value) {
		if (value != null) {
			switch (value) {
			case 0:
				return EnumStatusRetorno.ERROR;
			case 1:
				return EnumStatusRetorno.SUCCESS;
			case 2:
				return EnumStatusRetorno.UPDATE;
			default:
				return null;
			}
		}
		return null;
	}

	public EnumStatusRetorno getEnumStatusRetorno(String value) {
		if (value != null) {
			switch (value) {
			case "error":
				return EnumStatusRetorno.ERROR;
			case "success":
				return EnumStatusRetorno.SUCCESS;
			case "update":
				return EnumStatusRetorno.UPDATE;
			default:
				return null;
			}
		} 
		return null;
	}

}
