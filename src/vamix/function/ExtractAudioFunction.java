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
import vamix.ui.components.DownloadModule;
import vamix.ui.components.ImportDialog;
import vamix.ui.components.ExtractAudioDialog;

/**
 * Strip audio based on the download function
 */
public class ExtractAudioFunction implements IFunction {

	/**
	 * The various variables required to extract
	 */
	private Video input;
	private Audio output;

	private ExtractWorker worker = null;

	private ExtractAudioDialog dialog;

	public ExtractAudioFunction(ExtractAudioDialog stripAudioDialog) {
		dialog = stripAudioDialog;
	}

	/**
	 * Cancel the strip audio
	 */
	public void cancelSa() {
		if (worker != null) {
			worker.cancel(true);
			System.out.println("worker.cancel(true)");
		}
	}

	/**
	 * Called when the strip audio button is pushed, this checks that all
	 * required fields have been appropriately filled and if so, starts the
	 * stripping of the audio.
	 */
	public int canStrip(String outputName, String outputDirectory,
			boolean automatic) {
		if (worker != null) {
			return 9;// Need to wait until previous task is complete
		}
		if (outputDirectory == null) {
			return 2;// null output
		}
		if (outputName.contains(" ")) {
			return 5;// output contains spaces
		}
		if (!outputName.endsWith(".mp3")) {
			outputName = outputName + ".mp3"; // Append an mp3 extension if none
												// exists
		}

		// From here assume output is a valid file and create a new component
		// (Not saved yet)
		File f = new File(outputDirectory + Helper.SLASH + outputName);
		this.output = new Audio(f);

		// Check file does not already exist
		if (this.output.getFile().exists()) {
			return 4;
		}

		// Get the Video to be stripped from the component manager
		ComponentManager cm = ComponentManager.getInstance();
		Video video = cm.getVideo();

		if (video == null || video.getFile() == null) {
			return 10; // There isn't a valid video file to extract the audio
						// from
		} else {
			input = video;
		}

		// Check the input file is valid
		/*
		 * if (!Helper.checkType(input.getFile()).equals("audio/mpeg")) { //
		 * TODO: Update type to be movie type return 7; }
		 */

		// All appears to be in order

		worker = new ExtractWorker(this, input.getPath(), output.getPath());
		worker.execute();
		// TODO: Update GUI the process has started
		return 0;
	}

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

	@Override
	public void doDone(int exitValue) {
		// TODO: Update user the process is complete
		worker = null;
		System.gc();
		dialog.done(exitValue);

	}

}
