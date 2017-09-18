package anubis.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import anubis.response.ResponseException;

public class EncriptionUtils {

	public static String sha1Converter(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}
	
	public static boolean checkSha1Password(String passwordPlaintext, String storedHash) {
		boolean password_verified = false;

		if(null == storedHash || !storedHash.startsWith("$2a$")){
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
		}
		password_verified = BCrypt.checkpw(passwordPlaintext, storedHash);

		return(password_verified);
	}

	public static String converterMd5(String valor) {
		String resultado = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(valor.getBytes(), 0, valor.length());
			resultado = new BigInteger(1, m.digest()).toString(16);
			while (resultado.length() < 32) {
				resultado = "0" + resultado;
			}
		} catch (NoSuchAlgorithmException e) {
			throw new ResponseException(e, "utils.erro.criar.md5");
		}
		return resultado;
	}
	
	public static String converterMd5(byte[] valor) {
		String resultado = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(valor, 0, valor.length);
			resultado = new BigInteger(1, m.digest()).toString(16);
			while (resultado.length() < 32) {
				resultado = "0" + resultado;
			}
		} catch (NoSuchAlgorithmException e) {
			throw new ResponseException(e, "utils.erro.criar.md5");
		}
		return resultado;
	}

}
