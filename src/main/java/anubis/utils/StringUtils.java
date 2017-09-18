package anubis.utils;

import java.text.ParseException;
import java.util.List;

import javax.swing.text.MaskFormatter;

public class StringUtils {

	public static boolean isEmpty(String value) {
		if (value != null && !value.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static String[] split(String termo, int valor) {
		int loop = Math.round(termo.length() / valor);
		String[] vetor = new String[loop];
		int marcador = 0;
		for (int i = 0; i < loop; i++) {
			vetor[i] = termo.substring(marcador, marcador + valor);
			marcador += valor;
		}
		return vetor;
	}

	public static String abbreviate(String str, int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	public static String abbreviate(String str, int offset, int maxWidth) {
		if (str == null) {
			return null;
		}
		if (maxWidth < 4) {
			throw new IllegalArgumentException("Minimum abbreviation width is 4");
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		if (offset > str.length()) {
			offset = str.length();
		}
		if ((str.length() - offset) < (maxWidth - 3)) {
			offset = str.length() - (maxWidth - 3);
		}
		if (offset <= 4) {
			return str.substring(0, maxWidth - 3) + "...";
		}
		if (maxWidth < 7) {
			throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
		}
		if ((offset + (maxWidth - 3)) < str.length()) {
			return "..." + abbreviate(str.substring(offset), maxWidth - 3);
		}
		return "..." + str.substring(str.length() - (maxWidth - 3));
	}

	public static String trim(String text, List<String> trimByList) {

		try{
			int beginIndex = 0;
			int endIndex = text.length();
			boolean continua = true;

			while(continua){
				continua = false;
				for(String trimBy : trimByList){
					if(text.substring(beginIndex, endIndex).startsWith(trimBy)){
						beginIndex += trimBy.length();
						continua = true;
					}
				}
			}

			continua = true;

			while(continua){
				continua = false;
				for(String trimBy : trimByList){
					if(text.substring(0, endIndex).endsWith(trimBy)){
						endIndex += trimBy.length();
						continua = true;
					}
				}
			}

			return text.substring(beginIndex, endIndex);
		}catch(StringIndexOutOfBoundsException e){
			return null;
		}
	}

	public static String removerMascaraCpfCnpj(String cpfCnpj) {
		return cpfCnpj.replace(".", "").replaceAll("-", "").replace("/", "");
	}

	public static String inserirMascaraCpfCnpj(String cpfCnpj) throws Exception {
		try {
			MaskFormatter mask = new MaskFormatter();
			mask.setValueContainsLiteralCharacters(false);
			if(cpfCnpj.length() == 11) {
				mask.setMask("###.###.###-##");
				return mask.valueToString(cpfCnpj);
			}else if(cpfCnpj.length() == 14) {
				mask.setMask("##.###.###/####-##");
				return mask.valueToString(cpfCnpj);
			}else {
				throw new Exception("inserir.mascara.cpf.cnpj.tamanho.invalido.erro");	
			}
		} catch (ParseException e) {
			throw new Exception("inserir.mascara.cpf.cnpj.erro");
		}
	}

}
