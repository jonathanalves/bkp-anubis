package anubis.backup.ftp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FtpProperties {
	
	private String address;
	private Integer port;
	private String user;
	private String password;
	private String hostDir;
	private boolean isSSLAtive; 

	public FtpProperties() {
		super();
	}
	
}
