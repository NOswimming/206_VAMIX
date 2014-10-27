package vamix.function;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vamix.function.worker.DownloadWorker;
import vamix.ui.dialogs.DownloadModule;

/**
 * The Function class for Downloading files.
 * 
 * @see #IFunction
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class DownloadFunction implements IFunction {

	private SwingWorker<Void, String> worker;
	private String url;
	private String fileName;
	private String filePath;
	private String downloadDirectory;
	private DownloadModule dM;

	/**
	 * Gets all the variables for the function.
	 */
	public DownloadFunction(String url, String fileName, String downloadDirectory,
			DownloadModule dM) {
		
		this.url = url;
		this.dM = dM;
		this.fileName = fileName;
		this.downloadDirectory = downloadDirectory;
		
	}
	
	/**
	 * Does any error handling and other checks before starting the Worker class.
	 */
	public void execute() {

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
	 * Cancels the Worker class's process.
	 */
	public void cancel() {
		if (worker != null)
			worker.cancel(true);
	}

	/**
	 * Tells the GUI the process is complete.
	 */
	public void doDone(int exitValue) {
		dM.done(exitValue);
		worker = null;
		System.gc();
	}

	/**
	 *  Processes intermediate results from the Worker and then updates the GUI.
	 */
	@Override
	public void doProcess(String intermediateValue) {
		Pattern one = Pattern.compile("(\\d{1,3})%");
		Matcher matcher = one.matcher(intermediateValue);
		if (matcher.find()) {
			dM.setProgress(Integer.parseInt(matcher.group(1)));
		}

	}

}