package anubis.enumeration.system;

public enum EnumGender {

	MALE(true, "masculino", "crud.generic.nao.encontrado.masculino"),
	FEMALE(false, "feminino", "crud.generic.nao.encontrado.feminino");
	
	private boolean valueBoolean;
	private String valueString;
	private String messageKey;
	
	private EnumGender(boolean valueBoolean, String valueString, String messageKey) {
		this.valueBoolean = valueBoolean;
		this.valueString = valueString;
		this.messageKey = messageKey;
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

	public String getMessageKey() {
		return messageKey;
	}
}
