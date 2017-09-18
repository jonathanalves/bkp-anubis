package anubis.enumeration.system;

public enum EnumGender {

	MALE(true, "masculino"),
	FEMALE(false, "feminino");
	
	private boolean valueBoolean;
	private String valueString;
	
	
	private EnumGender(boolean valueBoolean, String valueString) {
		this.valueBoolean = valueBoolean;
		this.valueString = valueString;
	}

	
	public boolean isMale() {
		return valueBoolean;
	}
	
	public boolean isFemale() {
		return !valueBoolean;
	}
	
	public String getValueString() {
		return valueString;
	}
	
}
