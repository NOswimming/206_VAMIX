package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vamix.function.IFunction;

/**
 * The swing worker for the Download functionality.
 * It updates the DownloadFunction class through the methods in the AbstractWorker class that it inherits.
 * 
 * @see #DownloadFunction #AbstractWorker
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class DownloadWorker extends AbstractWorker {

	private String url;
	private String directory;

	/**
	 * Gets all the variables for the process to run.
	 */
	public DownloadWorker(IFunction function, String url, String directory) {
		this.function = function;
		this.url = url;
		this.directory = directory;
	}

	/**
	 * Runs the process in the background.
	 */
	@Override
	public Void doInBackground() {
		ProcessBuilder builder = new ProcessBuilder("wget", url, "-P",
				directory);

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
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
		}
		return null;
	}

}
