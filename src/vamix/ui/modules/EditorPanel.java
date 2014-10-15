package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JButton;

public class EditorPanel extends JPanel {

	ImportDialog dialog_Import;
	DownloadManagerFrame downloadManager;
	AddTextDialog addStart;
	AddTextDialog addEnd;
	StripAudioDialog stripAudio;
	/**
	 * Create the panel.
	 */
	public EditorPanel() {
		
		setLayout(new BorderLayout(3, 0));
		
		JPanel panel_Left = new JPanel();
		add(panel_Left, BorderLayout.WEST);
		
		JPanel panel_Right = new JPanel();
		add(panel_Right, BorderLayout.CENTER);
		
		panel_Left.setLayout(new BoxLayout(panel_Left, BoxLayout.Y_AXIS));
		
		// Left panel for all the function buttons
		
		Dimension btnMin = new Dimension(200, 35);
		Dimension btnPref = new Dimension(200, 35);
		
		dialog_Import = new ImportDialog();
		
		downloadManager = new DownloadManagerFrame();
		
		addStart = new AddTextDialog(AddTextDialog.Type.START);
		
		addEnd = new AddTextDialog(AddTextDialog.Type.END);
		
		stripAudio = new StripAudioDialog();
		
		JPanel panel_GridLayout = new JPanel();
		panel_Left.add(panel_GridLayout);
		panel_GridLayout.setLayout(new GridLayout(10, 1, 0, 0));
		
		JButton btn_Import = new JButton("Import Video...");
		panel_GridLayout.add(btn_Import);
		btn_Import.setMinimumSize(btnMin);
		btn_Import.setMinimumSize(btnPref);
		
		
		JButton btn_Download = new JButton("Download");
		panel_GridLayout.add(btn_Download);
		btn_Download.setMinimumSize(btnMin);
		btn_Download.setMinimumSize(btnPref);
		
		JButton btn_AddTextStart = new JButton("Add Text to Start");
		panel_GridLayout.add(btn_AddTextStart);
		btn_AddTextStart.setMinimumSize(btnMin);
		btn_AddTextStart.setMinimumSize(btnPref);
		
		JButton btn_AddTextEnd = new JButton("Add Text to End");
		panel_GridLayout.add(btn_AddTextEnd);
		btn_AddTextEnd.setMinimumSize(btnMin);
		btn_AddTextEnd.setMinimumSize(btnPref);
		
		JButton btn_StripAudio = new JButton("Strip Audio");
		panel_GridLayout.add(btn_StripAudio);
		btn_StripAudio.setMinimumSize(btnMin);
		btn_StripAudio.setMinimumSize(btnPref);
		
		Component verticalGlue = Box.createVerticalGlue();
		panel_Left.add(verticalGlue);
		
		btn_StripAudio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stripAudio.setVisible(true);
			}
		});
		
		btn_AddTextEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addEnd.setVisible(true);
			}
		});
		
		btn_AddTextStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addStart.setVisible(true);
			}
		});
		
		btn_Download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadManager.setVisible(true);
			}
		});
		
		btn_Import.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog_Import.setVisible(true);
			}
		});
		
		
		// Right panel for the player and files
		
		panel_Right.setLayout(new BoxLayout(panel_Right, BoxLayout.Y_AXIS));
		
		Player  editorPlayer = new Player();
		panel_Right.add(editorPlayer);
		
		JPanel panel = new JPanel();
		panel_Right.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	}

}
