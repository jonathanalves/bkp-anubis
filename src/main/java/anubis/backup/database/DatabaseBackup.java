package anubis.backup.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import anubis.backup.dropbox.DropBoxSendFiles;
import anubis.response.ResponseException;
import anubis.utils.DateUtils;
import anubis.utils.FileUtils;

public class DatabaseBackup {

	protected String directoryName;
	protected String backupName;
	protected Integer maxBackups;
	protected String pgDumpCommand;
	protected String loginDropbox;
	protected String tokenDropbox;
	
	public DatabaseBackup(String directoryName, String backupName, Integer maxBackups, String pgDumpCommand, String loginDropbox, String tokenDropbox) {
		this.directoryName = directoryName;
		this.backupName = backupName;
		this.maxBackups = maxBackups;
		this.pgDumpCommand = pgDumpCommand;
		this.loginDropbox = loginDropbox;
		this.tokenDropbox = tokenDropbox;
	}

	public void checkPgDumpCommand() {
		if(pgDumpCommand == null || pgDumpCommand.equals("Nenhum")) {
			throw new ResponseException("system.comando.dump.nao.configurado");
		}
		File pgdumpFile = new File(pgDumpCommand);
		if(!pgdumpFile.exists()) {
			throw new ResponseException("system.comando.dump.nao.encontrado");
		}
	}
	
	public void generateBackupDatabase(boolean isSendToDropbox, Calendar data) {
		try {
			checkPgDumpCommand();
			String pathFileBackup = createBackupBanco(data);
			removeOldsBackups();
			if(isSendToDropbox) {
				File fileBackup = new File(pathFileBackup);
				DropBoxSendFiles.send(fileBackup, this.loginDropbox, this.loginDropbox);
			}
		} catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	private String createBackupBanco(Calendar data) {
		String pathFileBackup = null;
		try {
			String nomeFileBackup = createNameBackup(data);
			File dirBackupDatabase = new File(directoryName);
			FileUtils.verifyDirExist(dirBackupDatabase);

			pathFileBackup = directoryName + FileUtils.SEPARATOR + nomeFileBackup;
			ProcessBuilder builder = new ProcessBuilder(
					pgDumpCommand,
					"-F", //format (c -> custom, p -> plain, t -> tar)
					"c",
					"-n", //schema
					"public",
					"-E", //enconding
					"UTF-8",
					"-f", //file output
					pathFileBackup
					);

			Map<String, String> env = builder.environment();

			env.put("PGHOST", System.getProperty("app.database.address"));
			env.put("PGPORT", System.getProperty("app.database.port"));
			env.put("PGDATABASE", System.getProperty("app.database.name"));
			env.put("PGUSER", System.getProperty("app.database.user"));
			env.put("PGPASSWORD", System.getProperty("app.database.password"));
			builder.redirectErrorStream(true);

			Process p = builder.start();
			int result = p.waitFor();
			if(result != 0) {
				throw new ResponseException("system.erro.realizar.backup.base.dados");
			}
		} catch (InterruptedException | IOException e) {
			throw new ResponseException(e, "system.erro.realizar.backup.base.dados");
		}
		return pathFileBackup;
	}

	public String createNameBackup(Calendar data) {
		StringBuilder nameBackup = new StringBuilder();
		nameBackup.append(backupName);
		nameBackup.append(" (");
		nameBackup.append(DateUtils.getDiaMesAnoHoraMinutoSegundoBackupFormat(data));
		nameBackup.append(").backup");
		return nameBackup.toString();
	}

	private void removeOldsBackups() {
		File dirBackupDatabase = new File(directoryName);
		String[] files = dirBackupDatabase.list();
		if(files.length > 1) {
			if(files.length > this.maxBackups) {
				List<FileBackup> filesBackup = new ArrayList<>();
				for(String file : files) {
					try {
						int beginIndex = file.lastIndexOf('(')+1;
						int endIndex = file.lastIndexOf(')');
						String dataString = file.substring(beginIndex, endIndex);
						Calendar data = DateUtils.getDiaMesAnoHoraMinutoSegundoBackupFormat(dataString);
						filesBackup.add(new FileBackup(data, file));
					} catch (Exception e) {
						File fileDelete = new File(directoryName + FileUtils.SEPARATOR + file);
						fileDelete.delete();
					}
				}

				Collections.sort(filesBackup);
				int countBackupDelete = files.length - this.maxBackups.intValue();
				for(int i=0; i<countBackupDelete; i++) {
					String nameFile = filesBackup.get(i).getName();
					File bkpFileDelete = new File(directoryName + FileUtils.SEPARATOR + nameFile);
					bkpFileDelete.delete();
				}
			}
		}
	}

}
