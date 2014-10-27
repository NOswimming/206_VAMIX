package vamix.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

import vamix.ui.dialogs.AddTextDialog;
import vamix.ui.dialogs.DownloadManagerFrame;
import vamix.ui.dialogs.ExtractAudioDialog;
import vamix.ui.dialogs.ImportDialog;
import vamix.ui.dialogs.OverlayAudioDialog;
import vamix.ui.dialogs.WorkingDirectoryDialog;
import vamix.ui.player.Player;

/**
 * Main GUI JPanel for VAMIX. 
 * Lays out the buttons on one side and the player on the other. 
 * Also contains all the dialogs that are shown by pressing the buttons.
 * 
 * @author Callum Fitt-Simpson
 *
 */
public class MainPanel extends JPanel implements ActionListener {

	DownloadManagerFrame frame_DownloadManager;

	WorkingDirectoryDialog dialog_WorkingDirectory;
	ImportDialog dialog_Import;
	AddTextDialog dialog_AddTextStart;
	AddTextDialog dialog_AddTextEnd;
	ExtractAudioDialog dialog_ExtractAudio;
	OverlayAudioDialog dialog_OverlayAudio;

	Player player;

	JButton btn_WorkingDirectory;
	JButton btn_Import;
	JButton btn_Download;
	JButton btn_AddTextStart;
	JButton btn_AddTextEnd;
	JButton btn_ExtractAudio;
	JButton btn_OverlayAudio;

	/**
	 * Create the panel.
	 */
	public MainPanel() {

		// Layout for the main panel which is split into a left and right panel

		setLayout(new BorderLayout(3, 0));

		JPanel panel_Left = new JPanel();
		add(panel_Left, BorderLayout.WEST);

		JPanel panel_Right = new JPanel();
		add(panel_Right, BorderLayout.CENTER);

		// Left panel for all the function buttons

		panel_Left.setLayout(new BoxLayout(panel_Left, BoxLayout.Y_AXIS));

		Dimension btnMin = new Dimension(200, 35);
		Dimension btnPref = new Dimension(200, 35);

		JPanel panel_GridLayout = new JPanel();
		panel_GridLayout.setLayout(new GridLayout(10, 1, 2, 2));
		panel_Left.add(panel_GridLayout);

		Component verticalGlue = Box.createVerticalGlue();
		panel_Left.add(verticalGlue);
		
		btn_WorkingDirectory = new JButton("Set Working Directory");
		panel_GridLayout.add(btn_WorkingDirectory);
		btn_WorkingDirectory.setMinimumSize(btnMin);
		btn_WorkingDirectory.setMinimumSize(btnPref);
		btn_WorkingDirectory.addActionListener(this);

		btn_Import = new JButton("Import Video");
		panel_GridLayout.add(btn_Import);
		btn_Import.setMinimumSize(btnMin);
		btn_Import.setMinimumSize(btnPref);
		btn_Import.addActionListener(this);

		btn_Download = new JButton("Download");
		panel_GridLayout.add(btn_Download);
		btn_Download.setMinimumSize(btnMin);
		btn_Download.setMinimumSize(btnPref);
		btn_Download.addActionListener(this);

		btn_AddTextStart = new JButton("Add Text to Start");
		panel_GridLayout.add(btn_AddTextStart);
		btn_AddTextStart.setMinimumSize(btnMin);
		btn_AddTextStart.setMinimumSize(btnPref);
		btn_AddTextStart.addActionListener(this);

		btn_AddTextEnd = new JButton("Add Text to End");
		panel_GridLayout.add(btn_AddTextEnd);
		btn_AddTextEnd.setMinimumSize(btnMin);
		btn_AddTextEnd.setMinimumSize(btnPref);
		btn_AddTextEnd.addActionListener(this);

		btn_ExtractAudio = new JButton("Extract Audio");
		panel_GridLayout.add(btn_ExtractAudio);
		btn_ExtractAudio.setMinimumSize(btnMin);
		btn_ExtractAudio.setMinimumSize(btnPref);
		btn_ExtractAudio.addActionListener(this);
		
		btn_OverlayAudio = new JButton("Overlay Audio");
		//panel_GridLayout.add(btn_OverlayAudio);  //TODO: fix functionality
		btn_OverlayAudio.setMinimumSize(btnMin);
		btn_OverlayAudio.setMinimumSize(btnPref);
		btn_OverlayAudio.addActionListener(this);

		// Right panel for the player

		panel_Right.setLayout(new BoxLayout(panel_Right, BoxLayout.Y_AXIS));

		player = new Player();
		panel_Right.add(player);

		// Instantiate the function dialogs components

		frame_DownloadManager = new DownloadManagerFrame();
		dialog_WorkingDirectory = new WorkingDirectoryDialog();
		dialog_Import = new ImportDialog();
		dialog_AddTextStart = new AddTextDialog(AddTextDialog.Type.START);
		dialog_AddTextEnd = new AddTextDialog(AddTextDialog.Type.END);
		dialog_ExtractAudio = new ExtractAudioDialog();
		dialog_OverlayAudio = new OverlayAudioDialog();

	}
	
	/**
	 * Display the WorkingDirectoryDialog at the front to be used first.
	 */
	public void showWorkingDirectoryDialog() {
		dialog_WorkingDirectory.setVisible(true);
		dialog_WorkingDirectory.setLocationRelativeTo(null);
		dialog_WorkingDirectory.toFront();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btn_WorkingDirectory) {
			dialog_WorkingDirectory.setLocationRelativeTo(null);
			dialog_WorkingDirectory.setVisible(true);
		}
		
		if (e.getSource() == btn_Import) {
			dialog_Import.setLocationRelativeTo(null);
			dialog_Import.setVisible(true);
		}

		if (e.getSource() == btn_Download) {
			frame_DownloadManager.setLocationRelativeTo(null);
			frame_DownloadManager.setVisible(true);
		}

		if (e.getSource() == btn_AddTextStart) {
			dialog_AddTextStart.setLocationRelativeTo(null);
			dialog_AddTextStart.setVisible(true);
		}

		if (e.getSource() == btn_AddTextEnd) {
			dialog_AddTextEnd.setLocationRelativeTo(null);
			dialog_AddTextEnd.setVisible(true);
		}

		if (e.getSource() == btn_ExtractAudio) {
			dialog_ExtractAudio.setLocationRelativeTo(null);
			dialog_ExtractAudio.setVisible(true);
		}
		
		if (e.getSource() == btn_OverlayAudio) {
			dialog_OverlayAudio.setLocationRelativeTo(null);
			dialog_OverlayAudio.setVisible(true);
		}

	}

}
