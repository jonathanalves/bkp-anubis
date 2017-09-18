package anubis.utils.test;

public enum ControllerIntegrationTestEnum {
	
	MESSAGE("message"),
	STATUS("status");
	
	private String valor;
	
	private ControllerIntegrationTestEnum(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
	
}
