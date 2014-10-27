package vamix.component;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import vamix.component.components.Audio;
import vamix.component.components.Text;
import vamix.component.components.Video;
import vamix.misc.Helper;

/**
 * The class for managing important files.
 * It is a singleton class so that the important files can be accessed easily from other classes,
 * and so that there aren't any conflicts in which files to use.
 * 
 * Manages the file being played by the player.
 * Manages the current working directory.
 * Manages any imported files.
 * Uses the Helper class to check imported files are video or audio files.
 * 
 * @see #Helper
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class ComponentManager {

	private static ComponentManager instance;

	private int ID;
	private List<Audio> audioList;
	private List<Text> textList;
	private Video videoInstance;
	private File playFile;
	private File workingDirectory;

	/**
	 * @return the Component Manager
	 */
	public static ComponentManager getInstance() {
		if (instance == null)
			instance = new ComponentManager();
		return instance;
	}

	/**
	 * private constructor to prevent instantiation.
	 */
	private ComponentManager() {
		ID = 0;
		audioList = new LinkedList<Audio>();
		textList = new LinkedList<Text>();
	}

	/**
	 * @return an ID for uniquely identifying a component
	 */
	private int getID() {
		ID++;
		return ID;
	}

	/**
	 * Attempts to import a file. Checks if it is a valid video or audio file.
	 * If it is valid then sets it as the next file to be played by the player.
	 * 
	 * @param file to be imported
	 * @return true if the file was successfully imported, otherwise false.
	 */
	public boolean importFile(String file) {

		File f = new File(file);

		if (!f.isFile()) {
			return false; // File is invalid
		}

		if (Helper.isVideo(f)) {
			setPlayFile(f);
			setVideo(f);
			return true;
		}

		if (Helper.isAudio(f)) {
			setPlayFile(f);
			addAudio(f);
			return true;
		}

		return false;
	}

	/**
	 * Sets the video or audio file to be played by the player.
	 */
	private void setPlayFile(File f) {
		playFile = f; // Set the file to be played with the player
	}

	/**
	 * Gets the video or audio file to be played by the player.
	 */
	public File getPlayFile() {
		return playFile;
	}

	/**
	 * Sets the video file for functions.
	 */
	private void setVideo(File f) {
		videoInstance = new Video(f);// Set the video to be edited
	}

	/**
	 * Gets the video file for functions.
	 */
	public Video getVideo() {
		return videoInstance;
	}

	/**
	 * Check the audio file isn't already managed by the Component Manager If it
	 * is then return the ID of the corresponding component
	 * 
	 * @param f
	 * @return
	 */
	private int addAudio(File f) {

		for (Audio a : audioList) {
			if (a.getFile() == f)
				return a.getID();
		}

		Audio a = new Audio(f);
		a.setID(getID());
		audioList.add(a);// Add the audio object to the list of objects to be
							// used
		return a.getID();

	}

	/**
	 * @param ID
	 *            : the unique ID of the Audio component
	 * @returns the Audio component
	 */
	public Audio getAudio(int ID) {
		for (Audio a : audioList) {
			if (a.getID() == ID)
				return a;
		}
		return null;
	}

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(File newWorkingDirectory) {
		if (!newWorkingDirectory.isDirectory())
			workingDirectory = newWorkingDirectory.getParentFile();
		else
			workingDirectory = newWorkingDirectory;

	}

}
