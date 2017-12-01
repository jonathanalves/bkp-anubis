package anubis.backup.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadErrorException;

public class DropBoxSendFiles {

	public static void send(File file, String loginDropbox, String tokenDropBox) throws UploadErrorException, DbxException, IOException {
		DbxRequestConfig config = new DbxRequestConfig(loginDropbox);
	    DbxClientV2 client = new DbxClientV2(config, tokenDropBox);
	     
	    try (InputStream in = new FileInputStream(file)) {
	    	client.files().uploadBuilder("/"+file.getName()).uploadAndFinish(in);
	    }
	}
	
}
