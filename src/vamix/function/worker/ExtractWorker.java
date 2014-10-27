package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import vamix.function.IFunction;

/**
 * The worker thread for the extract portion of Vamix, handles the actual
 * extracting of the file so the gui doesn't lock up.
 * 
 */
public class ExtractWorker extends Worker {
	
	private String inputFilepath;
	private String outputFilepath;
	
	public ExtractWorker (IFunction function, String inputFilepath, String outputFilepath) {
		this.function = function;
		this.inputFilepath = inputFilepath;
		this.outputFilepath = outputFilepath;
	}

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
