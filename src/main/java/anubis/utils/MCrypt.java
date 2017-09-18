package anubis.utils;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @url https://github.com/serpro/Android-PHP-Encrypt-Decrypt
 *
 */
public class MCrypt {

    static char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public MCrypt(String secretKey, String iv, boolean useDate) {
    	if(useDate) {
    		String data = new SimpleDateFormat("yyyyMMdd").format(new Date());
	        secretKey += data;
	        iv += data;
    	}
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");
        
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch ( NoSuchAlgorithmException e) {
        	System.out.println("Erro: Algoritmo de Encryption inexistent");
        } catch ( NoSuchPaddingException e) {
        	System.out.println("Erro NoSuchPaddingException");
        }
    }

    public MCrypt(String secretKey, String iv) {
    	
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch ( NoSuchAlgorithmException e) {
        	System.out.println("Erro: Algoritmo de Encryption inexistent");
        } catch ( NoSuchPaddingException e) {
        	System.out.println("Erro NoSuchPaddingException");
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] encrypted = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch ( Exception e) {
            e.printStackTrace();
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }
//
        byte[] decrypted = null;
//
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
            // Remove trailing zeroes
            if (decrypted.length > 0) {
                int trim = 0;
                for (int i = decrypted.length - 1; i >= 0; i--) {
                    if (decrypted[i] == 0) {
                        trim++;
                    }
                }

                if (trim > 0) {
                     byte[] newArray = new byte[decrypted.length - trim];
                    System.arraycopy(decrypted, 0, newArray, 0,
                            decrypted.length - trim);
                    decrypted = newArray;
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] buf) {
         char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
             int len = str.length() / 2;
             byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
         char paddingChar = 0;
         int size = 16;
         int x = source.length() % size;
         int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}