package vamix.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vamix.component.ComponentManager;
import vamix.component.components.Audio;
import vamix.component.components.Video;
import vamix.function.worker.ExtractWorker;
import vamix.misc.Helper;
import vamix.ui.dialogs.DownloadModule;
import vamix.ui.dialogs.ExtractAudioDialog;
import vamix.ui.dialogs.ImportDialog;

/**
 * The Function class for Extracting Audio.
 * 
 * @see #IFunction
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class ExtractAudioFunction implements IFunction {

	private Video input;
	private Audio output;

	private ExtractWorker worker = null;

	private ExtractAudioDialog dialog;
	
	private String outputName;
	private String outputDirectory;

	/**
	 * Gets all the variables for the function.
	 */
	public ExtractAudioFunction(ExtractAudioDialog stripAudioDialog, String outputName, String outputDirectory,
			boolean automatic) {
		
		dialog = stripAudioDialog;
		
		this.outputName = outputName;
		this.outputDirectory = outputDirectory;
	}
	
	/**
	 * Does any error handling and other checks before starting the Worker class.
	 */
	public void execute() {
		if (worker != null) {
			dialog.canExtractAudioExitValue(9);// Need to wait until previous task is complete
			return;
		}
		if (outputDirectory == null) {
			dialog.canExtractAudioExitValue(2);// null output
			return;
		}
		if (outputName.contains(" ")) {
			dialog.canExtractAudioExitValue(5);// output contains spaces
			return;
		}
		if (!outputName.endsWith(".mp3")) {
			outputName = outputName + ".mp3"; // Append an mp3 extension if none
			return;									// exists
		}

		// From here assume output is a valid file and create a new component
		File f = new File(outputDirectory + Helper.SLASH + outputName);
		this.output = new Audio(f);

		// Check file does not already exist
		if (this.output.getFile().exists()) {
			dialog.canExtractAudioExitValue(4);
			return;
		}

		// Get the Video to be stripped from the component manager
		ComponentManager cm = ComponentManager.getInstance();
		Video video = cm.getVideo();

		if (video == null || video.getFile() == null) {
			dialog.canExtractAudioExitValue(10); // There isn't a valid video file to extract the audio
			return;			// from
		} else {
			input = video;
		}

		worker = new ExtractWorker(this, input.getPath(), output.getPath());
		worker.execute();	
	}

	/**
	 * Cancels the Worker class's process.
	 */
	public void cancel() {
		if (worker != null) {
			worker.cancel(true);
			System.out.println("worker.cancel(true)");
		}
	}

	/**
	 *  Processes intermediate results from the Worker and then updates the GUI.
	 */
	@Override
	public void doProcess(String intermediateValue) {
		// Gets the duration of the video file to calculate the total
		// number of frames
		Pattern three = Pattern
				.compile("Duration:\\s(\\d{2}):(\\d{2}):(\\d{2})\\.\\d{2}");
		Matcher matcher = three.matcher(intermediateValue);
		if (matcher.find()) {
			int hours = Integer.parseInt(matcher.group(1));
			int mins = Integer.parseInt(matcher.group(2));
			int secs = Integer.parseInt(matcher.group(3));
			// Calculate the total number of seconds
			int totalSecs = hours * 360 + mins * 60 + secs;
			dialog.setDurationSeconds(totalSecs);
		}

		// Gets the number of frames per second to calculate the total
		// number of frames
		Pattern two = Pattern.compile("Video:.+\\s(\\d+)\\sfps");
		matcher = two.matcher(intermediateValue);
		if (matcher.find()) {
			int fps = Integer.parseInt(matcher.group(1));
			dialog.setFps(fps);
		}

		// Gets the frame that is being extracted
		Pattern one = Pattern.compile("frame=\\s*(\\d+)\\s");
		matcher = one.matcher(intermediateValue);
		if (matcher.find()) {
			int frame = Integer.parseInt(matcher.group(1));
			dialog.setProgress(frame);
		}

	}

	/**
	 * Tells the GUI the process is complete.
	 */
	@Override
	public void doDone(int exitValue) {
		worker = null;
		System.gc();
		dialog.done(exitValue);

	}

}
