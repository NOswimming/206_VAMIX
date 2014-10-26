package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import vamix.component.ComponentManager;

public class ImportDialog extends JDialog implements ActionListener {

	JTextField textField_FileChooser;
	JButton button_FileChooser;
	JButton btn_Import;
	
	List<ActionListener> confirmListeners = new LinkedList();

	private final JFileChooser fc = new JFileChooser();

	public ImportDialog() {
		setTitle("Import File");
		
		JPanel contentPanel = new JPanel();
		setContentPane(contentPanel);

		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JLabel lbl_Import = new JLabel("Import Video from File");
		lbl_Import.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lbl_Import);

		JPanel panel_FileChooser = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_FileChooser.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_FileChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPanel.add(panel_FileChooser);
		panel_FileChooser.setMinimumSize(new Dimension(450, 35));
		panel_FileChooser.setMaximumSize(new Dimension(2000, 40));
		panel_FileChooser.setPreferredSize(new Dimension(450, 35));

		JLabel lbl_File = new JLabel("File:");
		lbl_File.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_FileChooser.add(lbl_File);

		textField_FileChooser = new JTextField();
		textField_FileChooser.setEditable(false);
		panel_FileChooser.add(textField_FileChooser);
		textField_FileChooser.setColumns(20);

		button_FileChooser = new JButton("...");
		panel_FileChooser.add(button_FileChooser);
		button_FileChooser.addActionListener(this);

		btn_Import = new JButton("Import");
		panel_FileChooser.add(btn_Import);
		btn_Import.addActionListener(this);

		pack();
	}

	private void setVideoExitValue(int exitValue) {
		switch (exitValue) {
		case 0:
			JOptionPane.showMessageDialog(null,
					"Video file imported successfully.");
			updateConfirmListeners();
			break;
		case 1:
			JOptionPane.showMessageDialog(null,
					"No directory selected.\n Please select a valid file.");
			break;
		case 2:
			JOptionPane.showMessageDialog(null,
					"Invalid file selected.\n Please select a valid file.");
			break;
		default:
			break;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == button_FileChooser) {
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textField_FileChooser.setText(fc.getSelectedFile()
						.getAbsolutePath());
			}
		}

		if (e.getSource() == btn_Import) {
			if (textField_FileChooser.getText().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please select a video file to import.");
			} else {
				int result = ComponentManager.getInstance().setVideo(
						textField_FileChooser.getText());
				setVideoExitValue(result);
			}
		}

	}
	
	public void addConfirmListener(ActionListener listener) {
		confirmListeners.add(listener);
	}
	
	public void updateConfirmListeners () {
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "confirm");
		for (ActionListener al : confirmListeners) {
			al.actionPerformed(e);
		}
	}

}
