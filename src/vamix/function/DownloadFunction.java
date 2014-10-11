package vamix.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vamix.misc.Helper;
import vamix.ui.modules.DownloadModule;

/**
 * Handles downloading files, largely taken from Trob525's
 * assignment two code.
 */
public class DownloadFunction {

	private SwingWorker<Void, String> currentWorker = new Worker();
	private final String CMD = "wget";
	private String arg = "-c";
	private String url;
	private String fileName;
	private DownloadModule dM;

	public DownloadFunction(String url, String filename, DownloadModule dM) {
		this.url = url;
		this.dM = dM;
		fileName = filename;
		if(new File(filename).exists()) {
			Object[] options = {"Yes, complete the download.", "No, cancel the download."};
			int n = JOptionPane.showOptionDialog(dM,
					"The file specified is already partially downloaded, would you like to complete?",
					"Congratulations",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
		}
		currentWorker.execute();
	}

	/**
	 * Called when a file finishing downloading
	 */
	public void doDone(int exitValue) {
		dM.done(exitValue);
		currentWorker = null;
		System.gc();
	}

	/**
	 * Cancel the download
	 */
	public void cancelDl() {
		if (currentWorker != null)
			currentWorker.cancel(true);
	}

	/**
	 * The worker thread for the download portion of Vamix, handles the actual
	 * downloading of the file so the gui doesn't lock up.
	 *
	 */
	private class Worker extends SwingWorker<Void, String> {
		private Process process;
		private int exitValue;
		
		@Override
		public Void doInBackground() {
			ProcessBuilder builder = new ProcessBuilder(CMD, arg, "--progress=bar:force", "-O", fileName, url);
			builder.redirectErrorStream(true);
			try {
				process = builder.start();
			} catch (IOException e1) {e1.printStackTrace();}
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			try {
				while (!isCancelled() && (line = stdoutBuffered.readLine()) != null) {
					publish(line);
				}
			} catch (IOException e) {e.printStackTrace();}
			try {
				process.waitFor();
			} catch (InterruptedException e) {}
			return null;
		}

		@Override
		protected void process(List<String> list) {
			for (String s : list) {
				Pattern one = Pattern.compile("(\\d{1,3})%");
				Matcher matcher = one.matcher(s);
				if(matcher.find()) {
					dM.setProgress(Integer.parseInt(matcher.group(1)));
				}
			}
		}

		@Override
		public void done() {
			if (isCancelled()) {
				process.destroy();
				return;
			}
			exitValue = process.exitValue();
			doDone(exitValue);
		}
	}
}