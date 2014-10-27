package vamix.function.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.SwingWorker;

import vamix.function.IFunction;

public class DownloadWorker extends Worker {

	private String url;
	private String directory;

	public DownloadWorker(IFunction function, String url, String directory) {
		this.function = function;
		this.url = url;
		this.directory = directory;
	}

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
