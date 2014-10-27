package vamix.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import vamix.component.ComponentManager;
import vamix.function.ExtractAudioFunction;
import vamix.function.worker.AbstractWorker;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The GUI component for Extracting Audio from a Video.
 * It is shown by pressing the Extract Audio button on the MainPanel.
 * It calls the ExtractAudioFunction to run the avconv process.
 * 
 * @see #MainPanel #ExtractAudioFunction
 * 
 * @author Callum Fitt-Simpson
 */
public class ExtractAudioDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_Filename;
	private JTextField textField_Directory;
	private JButton btn_StripAudio;
	private JButton btn_CancelClose;
	private JProgressBar progressBar;

	private ImportDialog dialog_Import;

	private ExtractAudioFunction stripAudio;

	private boolean finished = true;

	private int fps = -1;
	private int durationSeconds = -1;
	private int durationFrames = 100;

	/**
	 * Create the dialog.
	 */
	public ExtractAudioDialog() {
		setTitle("Strip Audio");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		Dimension dim_PanelsMin = new Dimension(450, 35);
		Dimension dim_PanelsMax = new Dimension(2000, 40);
		Dimension dim_PanelsPref = new Dimension(450, 35);

		JPanel panel_Filename = new JPanel();
		panel_Filename.setMinimumSize(dim_PanelsMin);
		panel_Filename.setMaximumSize(dim_PanelsMax);
		panel_Filename.setPreferredSize(dim_PanelsPref);
		panel_Filename.setLayout(new FlowLayout());
		FlowLayout flowLayout = (FlowLayout) panel_Filename.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_Filename);

		JLabel lbl_Filename = new JLabel("          File Name");
		panel_Filename.add(lbl_Filename);

		textField_Filename = new JTextField();
		panel_Filename.add(textField_Filename);
		textField_Filename.setColumns(20);

		progressBar = new JProgressBar();
		contentPanel.add(progressBar);

		Component verticalGlue = Box.createVerticalGlue();
		contentPanel.add(verticalGlue);

		// Button panel

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btn_StripAudio = new JButton("Strip Audio");
		btn_StripAudio.setActionCommand("Strip Audio");
		btn_StripAudio.addActionListener(this);
		buttonPane.add(btn_StripAudio);
		getRootPane().setDefaultButton(btn_StripAudio);

		btn_CancelClose = new JButton("Close");
		btn_CancelClose.setActionCommand("Close");
		btn_CancelClose.addActionListener(this);
		buttonPane.add(btn_CancelClose);

		pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_StripAudio) {
			String name = textField_Filename.getText();

			if (name == null || name.equals("")) {
				JOptionPane.showMessageDialog(this,
						"Please complete the filename and select a directory.");
				return;
			}
			String directory = "/home/";
			File wd = ComponentManager.getInstance().getWorkingDirectory();
			if (wd != null) directory = wd.getAbsolutePath();
			stripAudio = new ExtractAudioFunction(this, name, directory, true);
			stripAudio.execute();
		}

		if (e.getSource() == btn_CancelClose) {
			if (!finished) {
				System.out.println("cancelSa()");
				stripAudio.cancel();
			} else {
				dispose();
			}
		}

	}

	/**
	 * Updates the GUI based on the ExtractAudioFunction output. 
	 */
	public void canExtractAudioExitValue(int exitValue) {
		switch (exitValue) {
		case 0:
			// The Strip Audio process has started
			btn_CancelClose.setText("Cancel");
			break;
		case 2:
			JOptionPane.showMessageDialog(this,
					" Please select a valid output directory.");
			break;
		case 4:
			JOptionPane
					.showMessageDialog(this,
							"Output file already exists.\n Please change the name of the output file.");
			break;
		case 5:
			JOptionPane.showMessageDialog(this,
					" Filename can not contain spaces.");
			break;
		case 7:
			JOptionPane.showMessageDialog(this,
					" Audio can not be extracted from this type of file.");
			break;
		case 9:
			JOptionPane.showMessageDialog(this,
					" Currently stripping audio. Please wait...");
			break;
		case 10:
			int response = JOptionPane
					.showConfirmDialog(this,
							"No video file to strip audio from.\n Would you like to import one?");
			if (response == JOptionPane.YES_OPTION) {
				if (dialog_Import != null) {
					dialog_Import.setVisible(false);
					dialog_Import = null;
				}
				dialog_Import = new ImportDialog();
				dialog_Import.setVisible(true);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Updates the user that the process is complete
	 */
	public void done(int exitValue) {
		switch (exitValue) {
		case 0:
			// The Strip Audio process has finished correctly
			JOptionPane.showMessageDialog(this, " Stripping audio complete!");
			break;
		case AbstractWorker.CANCELLED_EXIT_VALUE:
			JOptionPane.showMessageDialog(this,
					" Stripping audio has been cancelled.");
			break;
		default:
			break;
		}
		finished = true;
		btn_CancelClose.setText("Close");

	}

	/**
	 * Updates the progress bar
	 */
	public void setProgress(int i) {
		progressBar.setValue(i);
	}

	/**
	 * Sets the number of frames per second of the video file being stripped
	 */
	public void setFps(int fps) {
		this.fps = fps;
		setDurationFrames();
	}

	/**
	 * Sets the duration of the video file being stripped in seconds
	 */
	public void setDurationSeconds(int seconds) {
		durationSeconds = seconds;
		setDurationFrames();
	}

	/**
	 * Sets the duration of the video file being stripped in frames and then
	 * updates the progress bar if it can.
	 */
	private void setDurationFrames() {
		if (durationSeconds > 0 && fps > 0) {
			durationFrames = durationSeconds * fps;
			progressBar.setMaximum(durationFrames);
		}
	}

}
