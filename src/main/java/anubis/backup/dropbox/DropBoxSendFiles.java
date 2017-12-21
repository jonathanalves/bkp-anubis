package anubis.backup.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DropBoxSendFiles {

	public static void send(File file, String loginDropbox, String tokenDropBox) {
		DbxRequestConfig config = new DbxRequestConfig(loginDropbox);
		DbxClientV2 client = new DbxClientV2(config, tokenDropBox);

		try (InputStream in = new FileInputStream(file)) {
			client.files().uploadBuilder("/"+file.getName()).uploadAndFinish(in);
		} catch (FileNotFoundException e) {
			//FIXME PHILIPE VERIFICAR O ODIN LOGGER
//			OdinLogger.error(MessageSystem.getMessage("system.dropbox.arquivo.nao.encontrado"), e);
		} catch (IOException e) {
//			OdinLogger.error(MessageSystem.getMessage("system.dropbox.erro.ler.arquivo", file.getName()), e);
		} catch (DbxException e) {
//			OdinLogger.error(MessageSystem.getMessage("system.dropbox.erro.enviar.arquivo", file.getName()), e);
		}
	}

}