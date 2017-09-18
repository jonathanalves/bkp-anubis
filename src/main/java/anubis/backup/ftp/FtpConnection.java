package anubis.backup.ftp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import anubis.response.ResponseException;

public abstract class FtpConnection {

	public void sendAction(FtpProperties configuration, boolean isSecure) {
		FTPClient ftpClient = null;
        try {
        	if(isSecure) {
    			ftpClient = new FTPSClient();
    		} else {
    			ftpClient = new FTPClient();
    		}
            // Conectando ao servidor FTP
        	ftpClient.connect(configuration.getAddress(), configuration.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
            	throw new ResponseException("system.ftp.servidor.recusou.conexao", String.valueOf(replyCode));
            }
            
            // Realizando login
            boolean success = ftpClient.login(configuration.getUser(), configuration.getPassword());
            if (!success) {
            	throw new ResponseException("system.ftp.falha.login");
            } 
        	
        	// Transferindo arquivo para o servidor
        	executeCommand(ftpClient);
        	
        	// Realizando logout
        	ftpClient.logout();
        } catch (IOException | NoSuchAlgorithmException ex) {
        	throw new ResponseException(ex, "system.ftp.erro.acesso.servidor");
        } finally {
			if(ftpClient.isConnected()){
	        	try {
					ftpClient.disconnect();
				} catch (IOException e) { }
			}
		}
	}
	
	protected abstract void executeCommand(FTPClient ftpClient) throws IOException;
	
}