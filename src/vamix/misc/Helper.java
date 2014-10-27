package vamix.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

	/**
	 * Start with shortening some system values
	 */
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String SLASH = System.getProperty("file.separator");
	public static final String HOME = System.getProperty("user.home");
	public static final String TAB = "\t";
	public static final String DEFAULT_FILE = HOME + SLASH + ".vamix" + SLASH
			+ "log";
	public static final String DEFAULT_DIRECTORY = HOME + SLASH + ".vamix";

	/**
	 * Uses process builder to check a file type
	 * 
	 * @return the mime-type
	 * 
	 */
	public static String checkType(File f) {
		ProcessBuilder builder = new ProcessBuilder("file", "--mime-type",
				f.getPath());
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			try {
				process.waitFor();
			} catch (InterruptedException e) {
			}
			String line = stdoutBuffered.readLine();
			if (line.split(": ").length == 2) {
				return line.split(": ")[1];
			}
		} catch (IOException e1) {
		}
		return null;
	}

	/**
	 * 
	 * Turns integer representations of minutes and seconds into a string of
	 * xx:xx:xx form
	 * 
	 * @return the time in string format
	 */
	public static String formatTime(int minutes, int seconds) {
		String hours = "00";
		String mins = "00";
		String secs = "00";

		if ((minutes / 60) > 9) {
			hours = Integer.toString(minutes / 60);
		} else {
			hours = "0" + Integer.toString(minutes / 60);
		}
		if (((seconds / 60) + minutes % 60) > 9) {
			mins = Integer.toString(((seconds / 60) + minutes % 60));
		} else {
			mins = "0" + Integer.toString(((seconds / 60) + minutes % 60));
		}
		if (seconds % 60 > 9) {
			secs = Integer.toString(seconds % 60);
		} else {
			secs = "0" + Integer.toString(seconds % 60);
		}

		return hours + ":" + mins + ":" + secs;
	}

}
