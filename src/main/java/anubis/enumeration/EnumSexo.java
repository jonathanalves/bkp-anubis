package anubis.enumeration;

public enum EnumSexo {
	
	MASCULINO("enum.sexo.masculino"),
	FEMININO("enum.sexo.feminino"),
	OUTROS("enum.sexo.outros");
	
	private String valueString;
	
	private EnumSexo(String valueString) {
		this.valueString = valueString;
	}

	
	public String getValueString() {
		return valueString;
	}
	
	public static EnumSexo getEnumSexo(String value) {
		if (value != null) {			
			EnumSexo retorno = null;			
			for (EnumSexo tipo : EnumSexo.values()) {
				if(tipo.name().equalsIgnoreCase(value)){
					retorno = tipo;
				}
			}			
			return retorno;			
		}
		return null;
	}
}
