package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vamix.function.IFunction;

/**
 * The swing worker for the ExtractAudio functionality.
 * It updates the ExtractAudioFunction class through the methods in the AbstractWorker class that it inherits.
 * 
 * @see #ExtractAudioFunction #AbstractWorker
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class ExtractWorker extends AbstractWorker {
	
	private String inputFilepath;
	private String outputFilepath;
	
	/**
	 * Gets all the variables for the process to run.
	 */
	public ExtractWorker (IFunction function, String inputFilepath, String outputFilepath) {
		this.function = function;
		this.inputFilepath = inputFilepath;
		this.outputFilepath = outputFilepath;
	}

	/**
	 * Runs the process in the background.
	 */
	@Override
	public Void doInBackground() {
		ProcessBuilder builder = new ProcessBuilder("/usr/bin/avconv",
				"-i", inputFilepath, outputFilepath);
		
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
