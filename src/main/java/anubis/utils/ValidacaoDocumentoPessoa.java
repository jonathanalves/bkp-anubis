package anubis.utils;

import java.util.Arrays;
import java.util.Objects;

public class ValidacaoDocumentoPessoa {

    private static final String[] arrayCPF = {"00000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555", "66666666666", "77777777777", "88888888888","99999999999"};
    private static final String[] arrayCNPJ = {"00000000000000", "11111111111111", "22222222222222", "33333333333333", "44444444444444", "55555555555555", "66666666666666", "77777777777777", "88888888888888","99999999999999"};
    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static boolean idValidCpfOrCNPJ(String cpfORCNPJ){
        if (Objects.isNull(cpfORCNPJ) || cpfORCNPJ.length() > 18) return false;
        String cpfORCNPJOnlyNumber = cpfORCNPJ.replaceAll("\\D+", "");
        if (cpfORCNPJOnlyNumber.length() == 11 && isValidCPF(cpfORCNPJOnlyNumber)){
            return true;
        }
        if (cpfORCNPJOnlyNumber.length() == 14 && isValidCNPJ(cpfORCNPJOnlyNumber)){
            return true;
        }
        return false;
    }

    public static boolean isValidCPF(String cpf) {
        if (Objects.isNull(cpf))return false;
        cpf = cpf.replace(".", "").replaceAll("-", "");
        if ((cpf==null) || (cpf.length()!=11) || (Arrays.asList(arrayCPF).contains(cpf))) return false;
        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (Objects.isNull(cnpj))return false;
        cnpj = cnpj.replace(".", "").replaceAll("-", "").replaceAll("/", "");
        if ((cnpj==null)||(cnpj.length()!=14) || (Arrays.asList(arrayCNPJ).contains(cnpj))) return false;
        Integer digito1 = calcularDigito(cnpj.substring(0,12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0,12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0,12) + digito1.toString() + digito2.toString());
    }

//	public static void main(String[] args) {
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("866.792.019-0"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("000.000.000-0"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("999.999.999-99"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("702.577.680-76"));
//
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("00000000000000"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("90643178000143"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("87.477.693/0001-96"));
//		System.out.println(ValidacaoDocumentoPessoa.idValidCpfOrCNPJ("87.477.693/0001-96-0"));
//	}

}
