package anubis.utils;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import anubis.response.ResponseException;

public class FileUtils {

	public static String SEPARATOR = System.getProperties().get("file.separator").toString();
	
	public static void verifyDirExist(File file) {
		if(!file.exists() || !file.isDirectory()) {
			boolean result = file.mkdirs();
			if(!result) {
				throw new ResponseException("utils.erro.criar.diretorio", file.getAbsolutePath());
			}
		}
	}
	
	public static void deleteFile(File file) {
		if (file.exists()) {
			boolean result = file.delete();
			if(!result) {
				throw new ResponseException("utils.erro.deletar.arquivo", file.getName());
			}
		}
	}
	
	public static File searchFileInDirectory(File file, String fileName) {
		if(file.isDirectory()){
			for(File f : file.listFiles()) {
				if(f.getName().equals(fileName)) {
					return f;
				}
			}
		}
		return null;
	}
	
	public static File searchFilesInOrder(File file, String... files) {
		if(!file.exists()) {
			return null;
		}
		for(String fileName : files) {
			File found = searchFileInDirectory(file, fileName);
			if(found != null) {
				file = found;
			} else {
				return null;
			}
		}
		return file;
	}
	
	public static void saveFile(byte[] file, String pathName, String fileName) throws IOException {
        Path path = Paths.get(pathName + File.separator + fileName);
        Files.write(path, file);
	}
	
	public static String generateCode() {
		return new BigInteger(130, new SecureRandom()).toString(32) + DateUtils.getCalendar().getTimeInMillis();
	}
	
	public static String upload(String path, String name, MultipartFile file) {

		if (name.contains("/")) {
			throw new ResponseException("utils.separador.diretorio.nao.permitido");
		}

		String fileName = DateUtils.getDiaMesAno(DateUtils.getCalendar()) + "-" + generateCode();

		if (file.isEmpty()) {
			throw new ResponseException("utils.arquivo.especifico.vazio", name);
		}

		try {
			new File(path).mkdir();

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path + FileUtils.SEPARATOR + fileName)));
			FileCopyUtils.copy(file.getInputStream(), stream);
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseException(e, "utils.erro.enviar.arquivo", name);
		}

		return fileName;

	}
	
	public static boolean isImagemValida(byte[] bytes) {
		boolean valido = true;
		try {
		    Image image = ImageIO.read(new ByteArrayInputStream(bytes));
		    if (image == null) {
		        valido = false;
		    }
		} catch(IOException ex) {
		    valido = false;
		}
		return valido;
	}
	
}
