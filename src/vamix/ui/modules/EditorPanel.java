package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JButton;

public class EditorPanel extends JPanel {

	AddTextDialog addStart;
	StripAudioDialog stripAudio;
	/**
	 * Create the panel.
	 */
	public EditorPanel() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_Left = new JPanel();
		
		JPanel panel_Right = new JPanel();
		
		JSplitPane splitPane_Editor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel_Left,panel_Right);
		panel_Left.setLayout(new BoxLayout(panel_Left, BoxLayout.Y_AXIS));
		
		// Left panel for all the function buttons
		
		JButton btn_AddTextStart = new JButton("Add Text to Start");
		panel_Left.add(btn_AddTextStart, 0);
		
		btn_AddTextStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(addStart != null) {
					addStart.setVisible(false);
					addStart = null;
				}
				addStart = new AddTextDialog(AddTextDialog.Type.START);
				addStart.setVisible(true);
			}
		});
		
		JButton btn_AddTextEnd = new JButton("Add Text to End");
		panel_Left.add(btn_AddTextEnd, 1);
		
		JButton btn_StripAudio = new JButton("Strip Audio");
		panel_Left.add(btn_StripAudio, 2);
		
		btn_StripAudio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stripAudio != null) {
					stripAudio.setVisible(false);
					stripAudio = null;
				}
				stripAudio = new StripAudioDialog();
				stripAudio.setVisible(true);
			}
		});
		
		Component verticalGlue = Box.createVerticalGlue();
		panel_Left.add(verticalGlue);
		
		
		// Right panel for the player and files
		
		panel_Right.setLayout(new BoxLayout(panel_Right, BoxLayout.Y_AXIS));
		
		Player  editorPlayer = new Player();
		panel_Right.add(editorPlayer);
		
		JPanel panel = new JPanel();
		panel_Right.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		add(splitPane_Editor);

	}

}
