package vamix.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import vamix.component.components.Audio;
import vamix.component.components.Text;
import vamix.component.components.Video;
import vamix.misc.Helper;

/**
 * A singleton, holds references to each of the current components and handles
 * creation of components.
 */
public class ComponentManager {

	private static ComponentManager instance;

	public static ComponentManager getInstance() {
		if (instance == null)
			instance = new ComponentManager();
		return instance;
	}

	private ComponentManager() {
		ID = 0;
		audioList = new LinkedList<Audio>();
		textList = new LinkedList<Text>();
	}

	/**
	 * ID for uniquely identifying the components
	 */
	private int ID;

	private int getID() {
		ID++;
		return ID;
	}

	/*********
	 * LISTS *
	 *********/

	/**
	 * List of audio files on the current video file
	 */
	private List<Audio> audioList;

	/**
	 * List of text components on the current video file
	 */
	private List<Text> textList;

	/*************
	 * END LISTS *
	 *************/

	/*************
	 * VIDEO *
	 *************/
	private Video videoInstance;

	/**
	 * Returns the video instance, returns null if there is no video
	 */
	public Video getVideo() {
		return videoInstance;
	}

	/**
	 * 
	 * Tries to set the video file for editing, returns 0 if successful
	 * otherwise returns an error code.
	 * 
	 * @param dir
	 *            the location of the file
	 * @return the success code
	 */
	public int setVideo(String dir) {
		if (dir.equals("")) {// No directory selected
			return 1;
		}
		File f = new File(dir);
		if (!f.isFile()) {
			return 2;// File is invalid
		}
		// TODO: Finish me
		System.out.println(Helper.checkType(f));
		videoInstance = new Video(f);// Set the video to be edited
		return 0;
	}

	/*************
	 * AUDIO *
	 *************/

	/**
	 * @param dir
	 *            : the absolute path to the file.
	 * @throws FileNotFoundException
	 * @returns the ID of the Audio component.
	 */
	public int addAudio(String dir) throws FileNotFoundException {
		if (dir.equals("")) {
			throw new IllegalArgumentException(); // No directory selected
		}
		File f = new File(dir);
		if (!f.isFile()) {
			throw new FileNotFoundException(); // Not a valid file
		}
		// TODO: Finish me
		System.out.println(Helper.checkType(f));

		// Check the audio file isn't already managed by the Component Manager
		// If it is then return the ID of the corresponding component
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
	 * 
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

}
