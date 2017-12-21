package anubis.backup.database;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class FileBackup  implements Comparable<FileBackup> {
	private Calendar data;
	private String name;
	
	public FileBackup(Calendar data, String name) {
		super();
		this.data = data;
		this.name = name;
	}

	@Override
	public int compareTo(FileBackup o) {
		 return this.data.before(o.data) ? -1 : (this.data.after(o.data) ? +1 : 0);
	}
	
}
