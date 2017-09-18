package anubis.backup.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpSendFiles extends FtpConnection {

	private static FtpSendFiles ftpConfiguration;
	private FtpProperties configuration;
	private File[] files;
	
	public static synchronized void uploadFiles(FtpProperties configuration, File... files) {
		if(ftpConfiguration == null) {
			ftpConfiguration = new FtpSendFiles();
		}
		ftpConfiguration.configuration = configuration;
		ftpConfiguration.files = files;
		
		ftpConfiguration.sendAction(configuration, configuration.isSSLAtive());
	}

	@Override
	protected void executeCommand(FTPClient ftpClient) throws IOException {
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    	ftpClient.enterLocalPassiveMode();
    	for(File file : files) {
    		try(InputStream input = new FileInputStream(new File(file.getAbsolutePath()))){
    			ftpClient.storeFile(configuration.getHostDir() + file.getName(), input);
    	    }
    	}
	}
	
}
