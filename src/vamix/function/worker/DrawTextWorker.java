package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vamix.function.IFunction;

/**
 * The swing worker for the DrawText functionality.
 * It updates the DrawTextFunction class through the methods in the AbstractWorker class that it inherits.
 * 
 * @see #DrawTextFunction #AbstractWorker
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class DrawTextWorker extends AbstractWorker {

	private String inputFilepath;
	private String outputFilepath;
	private String beginingOrEnd;
	private String duration;
	private String fontFilePath;
	private String text;
	private String textPositionX;
	private String textPositionY;
	private String fontSize;
	private String fontColor;
	private String opacity;

	/**
	 * Gets all the variables for the process to run.
	 */
	public DrawTextWorker(IFunction function, String inputFilepath,
			String outputFilepath, String beginingOrEnd, String duration,
			String fontFilePath, String text, String textPositionX,
			String textPositionY, String fontSize, String fontColor,
			String opacity) {

		this.function = function;
		this.inputFilepath = inputFilepath;
		this.outputFilepath = outputFilepath;
		this.beginingOrEnd = beginingOrEnd;
		this.duration = duration;
		this.fontFilePath = fontFilePath;
		this.text = text;
		this.textPositionX = textPositionX;
		this.textPositionY = textPositionY;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
		this.opacity = opacity;

	}

	/**
	 * Runs the process in the background.
	 */
	@Override
	public Void doInBackground() {
		
		ProcessBuilder builder = new ProcessBuilder("/usr/bin/avconv", "-i",
				inputFilepath, "-vf", "drawtext=fontfile='" + fontFilePath
						+ "':text='" + text + "':x=" + textPositionX + ":y="
						+ textPositionY + ":fontsize=" + fontSize
						+ ":fontcolor=" + fontColor + "@" + opacity + ":draw='" + beginingOrEnd
						+ "(t," + duration + ")'",
						"-strict", "experimental", "-f", "mp4","-y", outputFilepath);

		builder.redirectErrorStream(true);
		try {
			process = builder.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(
				new InputStreamReader(stdout));
		String line = null;
		try {
			while (!isCancelled() && (line = stdoutBuffered.readLine()) != null) {
				publish(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
		}
		return null;
	}
}
