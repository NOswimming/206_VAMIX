package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import vamix.function.DownloadFunction;
import vamix.function.StripAudioFunction;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StripAudioDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_Filename;
	private JTextField textField_Directory;
	private JButton btn_FileChooser;
	private JButton btn_StripAudio;
	private JButton btn_CancelClose;
	private JProgressBar progressBar;

	private final JFileChooser fc = new JFileChooser();

	private ImportDialog dialog_Import;

	private StripAudioFunction stripAudio;
	
	/**
	 * If the strip audio is finished and waiting for closure
	 */
	private boolean finished = false;
	
	private int fps = -1;
	private int durationSeconds = -1;
	private int durationFrames = 100;

	/**
	 * Create the dialog.
	 */
	public StripAudioDialog() {
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

		JPanel panel_Directory = new JPanel();
		panel_Directory.setMinimumSize(dim_PanelsMin);
		panel_Directory.setMaximumSize(dim_PanelsMax);
		panel_Directory.setPreferredSize(dim_PanelsPref);
		panel_Directory.setLayout(new FlowLayout());
		FlowLayout flowLayout2 = (FlowLayout) panel_Directory.getLayout();
		flowLayout2.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_Directory);

		JLabel lbl_Directory = new JLabel("Output Directory");
		panel_Directory.add(lbl_Directory);

		textField_Directory = new JTextField();
		textField_Directory.setColumns(20);
		textField_Directory.setEditable(false);
		panel_Directory.add(textField_Directory);

		btn_FileChooser = new JButton("...");
		panel_Directory.add(btn_FileChooser);

		btn_FileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(StripAudioDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textField_Directory.setText(fc.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		
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
			String directory = textField_Directory.getText();

			if (name == null || directory == null || name.equals("")
					|| directory.equals("")) {
				JOptionPane.showMessageDialog(this,
						"Please complete the filename and select a directory.");
				return;
			}
			stripAudio = new StripAudioFunction(this);
			int result = stripAudio.canStrip(name, directory, true);
			canStripExitValue(result);
		}
		
		if (e.getSource() == btn_CancelClose) {
			if(!finished) {
				System.out.println("cancelSa()");
				stripAudio.cancelSa();
			} else {
				dispose();
			}
		}

	}

	public void canStripExitValue(int exitValue) {
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
			JOptionPane.showMessageDialog(this,
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

	public void done(int exitValue) {
		switch (exitValue) {
		case 0:
			// The Strip Audio process has finished correctly
			JOptionPane.showMessageDialog(this, " Stripping audio complete!");
			break;
		case -1:
			// Cancelled
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
	 * @param i
	 */
	public void setProgress(int i) {
		progressBar.setValue(i);
	}
	
	/**
	 * Sets the number of frames per second of the video file being stripped
	 * @param seconds
	 */
	public void setFps(int fps) {
		this.fps = fps;
		setDurationFrames();
	}
	
	/**
	 * Sets the duration of the video file being stripped in seconds
	 * @param seconds
	 */
	public void setDurationSeconds(int seconds) {
		durationSeconds = seconds;
		setDurationFrames();
	}
	
	/**
	 * Sets the duration of the video file being stripped in frames and then updates the progress bar if it can.
	 */
	private void setDurationFrames() {
		if(durationSeconds > 0 && fps > 0) {
			durationFrames = durationSeconds*fps;
			progressBar.setMaximum(durationFrames);
		}
	}

}
