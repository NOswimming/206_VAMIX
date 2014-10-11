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
import vamix.misc.Helper;
import vamix.ui.modules.DownloadModule;
import vamix.ui.modules.ImportDialog;
import vamix.ui.modules.StripAudioDialog;

/**
 * Strip audio based on the download function
 */
public class StripAudioFunction {

	/**
	 * The various variables required to extract
	 */
	private Video input;
	private Audio output;

	private Worker worker = null;

	private StripAudioDialog dialog;

	public StripAudioFunction(StripAudioDialog stripAudioDialog) {
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
		if (!Helper.checkType(input.getFile()).equals("audio/mpeg")) {
		// TODO: Update type to be movie type
			return 7;
		}
		*/
		
		// All appears to be in order

		worker = new Worker();
		worker.execute();
		// TODO: Update GUI the process has started
		return 0;
	}

	/**
	 * Called upon completion or failure of an extract process
	 */
	private void doDone(int exitValue) {
		// TODO: Update user the process is complete
		worker = null;
		System.gc();
		dialog.done(exitValue);
	}

	/**
	 * The worker thread for the extract portion of Vamix, handles the actual
	 * extracting of the file so the gui doesn't lock up.
	 * 
	 */
	private class Worker extends SwingWorker<Void, String> {
		private Process process;
		private int exitValue;

		@Override
		public Void doInBackground() {
			ProcessBuilder builder = new ProcessBuilder("avconv", "-i",
					input.getPath(), output.getPath());
			builder.redirectErrorStream(true);
			try {
				process = builder.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
				System.out.println(s); //TODO: remove this line
				
				//Gets the duration of the video file to calculate the total number of frames
				Pattern three = Pattern.compile("Duration:\\s(\\d{2}):(\\d{2}):(\\d{2})\\.\\d{2}");
				Matcher matcher = three.matcher(s);
				if(matcher.find()) {
					int hours = Integer.parseInt(matcher.group(1));
					int mins = Integer.parseInt(matcher.group(2));
					int secs = Integer.parseInt(matcher.group(3));
					// Calculate the total number of seconds
					int totalSecs = hours*360 + mins*60 + secs;
					dialog.setDurationSeconds(totalSecs);
				}
				
				//Gets the number of frames per second to calculate the total number of frames
				Pattern two = Pattern.compile("Video:.+\\s(\\d+)\\sfps");
				matcher = two.matcher(s);
				if(matcher.find()) {
					int fps = Integer.parseInt(matcher.group(1));
					dialog.setFps(fps);
				}
				
				//Gets the frame that is being extracted
				Pattern one = Pattern.compile("frame=\\s*(\\d+)\\s");
				matcher = one.matcher(s);
				if(matcher.find()) {
					int frame = Integer.parseInt(matcher.group(1));
					dialog.setProgress(frame);
				}
			}
		}

		@Override
		public void done() {
			if (isCancelled()) {
				process.destroy();
				exitValue = -1;
				doDone(exitValue);
				return;
			}
			exitValue = process.exitValue();
			doDone(exitValue);
		}
	}

}
