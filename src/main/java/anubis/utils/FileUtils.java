package anubis.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
	
	public static void getFile(HttpServletResponse response, String path, String nameFile) {
		InputStream in = null;
		ServletOutputStream out = null;
		try {
			
			File file = new File(path);
			if(!file.exists()) {
				throw new ResponseException("utils.arquivo.nao.encontrado");
			}
			
			String name = ((nameFile!=null) ? nameFile : file.getName());
			
			in = new BufferedInputStream(new FileInputStream(file));
			response.setHeader("Content-Disposition", "attachment; filename=" + name );
			out = response.getOutputStream();
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseException("utils.arquivo.nao.encontrado");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new ResponseException("utils.erro.fechar.arquivo");
			}
		}
	}
	
	public static void getFile(HttpServletResponse response, byte[] bytes, String fileName) {
		InputStream in = null;
		ServletOutputStream out = null;
		try {
			in = new BufferedInputStream(new ByteArrayInputStream(bytes));
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			String mimeType = URLConnection.guessContentTypeFromStream(in);
			response.setContentType(mimeType);
			out = response.getOutputStream();
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
}
