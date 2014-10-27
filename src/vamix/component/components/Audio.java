package vamix.component.components;

import java.io.File;

/**
 * The class for encapsulating Audio files.
 * Used by the Component Manager
 * 
 * @see #ComponentManager
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class Audio {
	
	private int ID;
	private String path;
	private File file;

	public Audio(File f) {
		setFile(f);
		setPath(f.getAbsolutePath());
	}

	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
