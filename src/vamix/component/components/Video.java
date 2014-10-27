package vamix.component.components;

import java.io.File;

/**
 * Represents the current video file being edited
 */
public class Video {

	public Video(File f) {
		setFile(f);
		setPath(f.getAbsolutePath());
	}

	/**********
	 * FIELDS *
	 **********/

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
	 * Represents the java file that video is
	 */
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Represents the number of frames the video has
	 */
	private int frameCount;

	// TODO: Add more required video file information
	// if you have to get it once, may as well just save
	// it in case you need to get it again

}
