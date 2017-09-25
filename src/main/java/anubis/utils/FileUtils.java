package anubis.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	
}
