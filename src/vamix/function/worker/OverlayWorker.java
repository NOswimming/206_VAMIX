package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vamix.function.IFunction;

/**
 * The swing worker for the OverlayText functionality.
 * It updates the OverlayTextFunction class through the methods in the AbstractWorker class that it inherits.
 * 
 * @see #OverlayTextFunction #AbstractWorker
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class OverlayWorker extends AbstractWorker {
	
	private String inputFilepath;
	private String outputFilepath;
	private String audioFilepath;
	
	/**
	 * Gets all the variables for the process to run.
	 */
	public OverlayWorker (IFunction function, String inputFilepath, String outputFilepath, String audioFile) {
		this.function = function;
		this.inputFilepath = inputFilepath;
		this.outputFilepath = outputFilepath;
		this.audioFilepath = audioFile;
	}

	/**
	 * Runs the process in the background.
	 */
	@Override
	public Void doInBackground() {
		ProcessBuilder builder = new ProcessBuilder("/usr/bin/avconv",
				"-i", inputFilepath, "-i", audioFilepath, "-filter_complex", "amix=inputs=2", 
				"-strict", "experimental", "-f" ,"mp4", outputFilepath);
		
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
			while (!isCancelled()
					&& (line = stdoutBuffered.readLine()) != null) {
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
