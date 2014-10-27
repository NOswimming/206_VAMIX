package vamix.function;


import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vamix.component.ComponentManager;
import vamix.component.components.Audio;
import vamix.component.components.Video;
import vamix.function.worker.OverlayWorker;
import vamix.misc.Helper;
import vamix.ui.dialogs.OverlayAudioDialog;

/**
 * The Function class for Overlaying Audio.
 * 
 * @see #IFunction
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class OverlayAudioFunction implements IFunction {

	private Video input;
	private Audio output;

	private OverlayWorker worker = null;

	private OverlayAudioDialog dialog;
	
	private String outputName;
	private String outputDirectory;
	private String audioFile;

	/**
	 * Gets all the variables for the function.
	 */
	public OverlayAudioFunction(OverlayAudioDialog stripAudioDialog, String outputName, String outputDirectory,
			String audioFile) {
		
		dialog = stripAudioDialog;
		
		this.outputName = outputName;
		this.outputDirectory = outputDirectory;
		this.audioFile = audioFile;
	}
	
	/**
	 * Does any error handling and other checks before starting the Worker class.
	 */
	public void execute() {
		if (worker != null) {
			dialog.exitValue(9);// Need to wait until previous task is complete
			return;
		}
		if (outputDirectory == null) {
			dialog.exitValue(2);// null output
			return;
		}
		if (outputName.contains(" ")) {
			dialog.exitValue(5);// output contains spaces
			return;
		}
		if (!outputName.endsWith(".mp4")) {
			outputName = outputName + ".mp4"; // Append an mp4 extension if none
			return;									// exists
		}

		// From here assume output is a valid file and create a new component
		File f = new File(outputDirectory + Helper.SLASH + outputName);
		this.output = new Audio(f);

		// Check file does not already exist
		if (this.output.getFile().exists()) {
			dialog.exitValue(4);
			return;
		}

		// Get the Video to be overlayed from the component manager
		ComponentManager cm = ComponentManager.getInstance();
		Video video = cm.getVideo();

		if (video == null || video.getFile() == null) {
			dialog.exitValue(10); // There isn't a valid video file to extract the audio
			return;			// from
		} else {
			input = video;
		}

		worker = new OverlayWorker(this, input.getPath(), output.getPath(), audioFile);
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
