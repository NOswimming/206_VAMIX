package vamix.function;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vamix.component.ComponentManager;
import vamix.component.components.Video;
import vamix.function.worker.DrawTextWorker;
import vamix.misc.Helper;
import vamix.ui.dialogs.AddTextDialog;

/**
 * The Function class for Drawing Text on video files.
 * 
 * @see #IFunction
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class DrawTextFunction implements IFunction {

	private Video input;
	private Video output;

	public static final String BEGINING = "lt";
	public static final String END = "gt";

	private String beginingOrEnd;
	private String fontFilePath;
	private String text;
	private String fontColor;
	private String fontSize;
	private String outputName;
	private String outputDirectory;
	
	private String duration = "6";
	private String outputFileName = "StripAudioTest";
	private String outputFileExtension = ".avi";
	private String opacity = "1.0";
	private String textPositionY = "100";
	private String textPositionX = "50";
	

	private DrawTextWorker worker = null;

	private AddTextDialog dialog;
	

	/**
	 * Gets all the variables for the function.
	 */
	public DrawTextFunction(AddTextDialog dialog, String beginingOrEnd, String outputName, String outputDirectory,
			String text, String fontColor, String fontSize, String fontFilePath) {
		
		this.dialog = dialog;
		this.beginingOrEnd = beginingOrEnd;
		this.outputName = outputName;
		this.outputDirectory = outputDirectory;
		this.text = text;
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		this.fontFilePath = fontFilePath;
		
	}
	
	/**
	 * Does any error handling and other checks before starting the Worker class.
	 */
	public void execute() {

		if (worker != null) {
			dialog.canDrawTextExitValue(9);// Need to wait until previous task is complete
			return;
		}
		if (outputDirectory == null) {
			dialog.canDrawTextExitValue(2);// null output
			return;
		}
		if (outputName.contains(" ")) {
			dialog.canDrawTextExitValue(5);// output contains spaces
			return;
		}

		// From here assume output is a valid file and create a new component
		File f = new File(outputDirectory + Helper.SLASH + outputName);
		this.output = new Video(f);

		// Get the Video to draw the text on from the component manager
		ComponentManager cm = ComponentManager.getInstance();
		Video video = cm.getVideo();

		if (video == null || video.getFile() == null) {
			dialog.canDrawTextExitValue(10); // There isn't a valid video file to extract the audio
			return;		// from
		} else {
			input = video;
		}

		worker = new DrawTextWorker(this, input.getPath(), output.getPath(), beginingOrEnd, duration, this.fontFilePath, this.text, textPositionX, 
				textPositionY, this.fontSize, this.fontColor, opacity);
		worker.execute();
		dialog.canDrawTextExitValue(0);
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
		Pattern two = Pattern.compile("Stream:.+\\s(\\d+)\\sfps");
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