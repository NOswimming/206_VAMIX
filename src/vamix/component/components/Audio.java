package vamix.component.components;

import java.io.File;

/**
 * Represents an audio file object held by the ComponentManager
 */
public class Audio {

	public Audio(File f) {
		setFile(f);
		setPath(f.getAbsolutePath());
	}

	/**********
	 * FIELDS *
	 **********/

	/**
	 * Represents the Unique ID of the Audio object
	 */
	private int ID;

	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}

	/**
	 * Represents the location of the file being edited
	 */
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Represents the audio file in Java
	 */
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
