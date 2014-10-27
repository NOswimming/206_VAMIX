package vamix.component.components;

import java.io.File;

/**
 * The class for encapsulating Video files.
 * Used by the Component Manager
 * 
 * @see #ComponentManager
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class Video {
	
	private String path;
	private File file;
	private int frameCount;

	public Video(File f) {
		setFile(f);
		setPath(f.getAbsolutePath());
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
