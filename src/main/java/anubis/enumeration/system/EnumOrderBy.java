package anubis.enumeration.system;

public enum EnumOrderBy {

    ID("id"), NOME("nome"), ORDEM("ordem");
    
    private String valor;
    
    private EnumOrderBy(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

}