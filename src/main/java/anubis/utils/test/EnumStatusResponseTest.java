package anubis.utils.test;

public enum EnumStatusResponseTest {

	ERROR("ERROR"),
	SUCCESS("SUCCESS");
	
	private String valor;
	
	private EnumStatusResponseTest(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
	
}
