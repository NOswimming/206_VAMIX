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

import vamix.function.worker.DownloadWorker;
import vamix.function.worker.Worker;
import vamix.misc.Helper;
import vamix.ui.components.DownloadModule;

/**
 * Handles downloading files, largely taken from Trob525's assignment two code.
 */
public class DownloadFunction implements IFunction {

	private SwingWorker<Void, String> worker;
	private final String command = "wget";
	private String arguments;
	private String url;
	private String fileName;
	private String filePath;
	private String downloadDirectory;
	private DownloadModule dM;

	public DownloadFunction(String url, String downloadDirectory,
			DownloadModule dM) {
		
		this.url = url;
		this.dM = dM;
		this.downloadDirectory = downloadDirectory;

		// Pull the filename from the URL.
		Pattern pattern = Pattern.compile(".*/([^/]*+)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			fileName = matcher.group(1);
			matcher.group(1);
		}
		filePath = downloadDirectory + fileName;

		if (new File(this.filePath).exists()) {
			Object[] options = { "Yes, complete the download.",
					"No, cancel the download." };
			int n = JOptionPane
					.showOptionDialog(
							dM,
							"The file specified is already partially downloaded, would you like to complete?",
							"Congratulations", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
		}

		worker = new DownloadWorker(this, this.url, this.downloadDirectory);
		worker.execute();
	}

	/**
	 * Cancel the download
	 */
	public void cancelDl() {
		if (worker != null)
			worker.cancel(true);
	}

	/**
	 * Called when a file is finished downloading
	 */
	public void doDone(int exitValue) {
		dM.done(exitValue);
		worker = null;
		System.gc();
	}

	@Override
	public void doProcess(String intermediateValue) {
		Pattern one = Pattern.compile("(\\d{1,3})%");
		Matcher matcher = one.matcher(intermediateValue);
		if (matcher.find()) {
			dM.setProgress(Integer.parseInt(matcher.group(1)));
		}

	}

}