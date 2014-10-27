package vamix.ui.components;

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

public class MainPanel extends JPanel implements ActionListener {

	DownloadManagerFrame frame_DownloadManager;

	ImportDialog dialog_Import;
	AddTextDialog dialog_AddTextStart;
	AddTextDialog dialog_AddTextEnd;
	ExtractAudioDialog dialog_StripAudio;

	Player player;

	JButton btn_Import; // TODO: Remove this button
	JButton btn_Download;
	JButton btn_AddTextStart;
	JButton btn_AddTextEnd;
	JButton btn_StripAudio;

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

		// TODO: Remove this button
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

		btn_StripAudio = new JButton("Strip Audio");
		panel_GridLayout.add(btn_StripAudio);
		btn_StripAudio.setMinimumSize(btnMin);
		btn_StripAudio.setMinimumSize(btnPref);
		btn_StripAudio.addActionListener(this);

		// Right panel for the player and files

		panel_Right.setLayout(new BoxLayout(panel_Right, BoxLayout.Y_AXIS));

		player = new Player();
		panel_Right.add(player);

		// Instantiate the function dialogs components

		frame_DownloadManager = new DownloadManagerFrame();
		dialog_Import = new ImportDialog();
		dialog_AddTextStart = new AddTextDialog(AddTextDialog.Type.START);
		dialog_AddTextEnd = new AddTextDialog(AddTextDialog.Type.END);
		dialog_StripAudio = new ExtractAudioDialog();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_Import) {
			dialog_Import.setVisible(true);
		}

		if (e.getSource() == btn_Download) {
			frame_DownloadManager.setVisible(true);
		}

		if (e.getSource() == btn_AddTextStart) {
			dialog_AddTextStart.setVisible(true);
		}

		if (e.getSource() == btn_AddTextEnd) {
			dialog_AddTextEnd.setVisible(true);
		}

		if (e.getSource() == btn_StripAudio) {
			dialog_StripAudio.setVisible(true);
		}

	}

}
