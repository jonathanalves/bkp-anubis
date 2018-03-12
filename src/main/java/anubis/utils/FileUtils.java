package anubis.utils;

import anubis.response.ResponseException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

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

		validar(name, file);
		String fileName = processFileName(name, file);

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

	private static String processFileName(String fileName, MultipartFile file) {
		if (fileName == null || fileName.isEmpty()) {
			return generateFileName(file.getOriginalFilename());
		}
		fileName = generateFileName(fileName);
		String type = getFileType(file);
		if( !fileName.contains(type) ) {
			fileName += type;
		}
		return fileName;
	}

	private static String generateFileName(String name) {
		return generateCode().substring(0, 8) + "-" + name;
	}

	private static void validar(String name, MultipartFile file) {
		if (name.contains("/")) {
			throw new ResponseException("utils.separador.diretorio.nao.permitido");
		}

		if (file.isEmpty()) {
			throw new ResponseException("utils.arquivo.especifico.vazio", name);
		}
	}

	private static String getFileType(MultipartFile file) {
		String multipartFileName = file.getOriginalFilename();
		String type = multipartFileName.substring( multipartFileName.indexOf("."), multipartFileName.length());
		return type;
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
	
	public static void getFile(HttpServletResponse response, String path, String nameFile) {
		getFile(response, path, nameFile, true);
	}
	
	public static void getFile(HttpServletResponse response, String path, String nameFile, boolean forceDownload) {
		InputStream in = null;
		ServletOutputStream out = null;
		try  {
			
			File file = new File(path);
			if(!file.exists()) {
				throw new ResponseException("utils.arquivo.nao.encontrado");
			}
			
			String name = ((nameFile!=null) ? nameFile : file.getName());
			
			in = new BufferedInputStream(new FileInputStream(file));
			
			if(forceDownload) {
				response.setHeader("Content-Disposition", "attachment; filename=" + name );
			}
			
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

	public static File escreverConteudoArquivo(File file, String conteudo){
		try (FileWriter fw = new FileWriter( file )) {
			fw.write(conteudo);
		} catch (IOException e) {
			throw new ResponseException("utils.erro.escrever.conteudo.arquivo");
		}
		return file;
	}

}
