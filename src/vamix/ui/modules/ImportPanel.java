package vamix.ui.modules;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ImportPanel extends JPanel {
	
	JTextField textField_FileChooser;
	private final JFileChooser fc = new JFileChooser();

	/**
	 * Create the panel.
	 */
	public ImportPanel() {
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel lbl_Import = new JLabel("Import Video from File");
		lbl_Import.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(lbl_Import);

		JPanel panel_FileChooser = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_FileChooser.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_FileChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(panel_FileChooser);
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

		JButton button_FileChooser = new JButton("...");
		panel_FileChooser.add(button_FileChooser);
		button_FileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(ImportPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textField_FileChooser.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JButton btn_Import = new JButton("Import");
		panel_FileChooser.add(btn_Import);
		btn_Import.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!textField_FileChooser.getText().equals("")) {
					int result = ComponentManager.getInstance().setVideo(textField_FileChooser.getText());
					setVideoExitValue(result);
				} else {
					JOptionPane.showMessageDialog(ImportPanel.this, "Please select a video file to import.");
				}
			}

			
		});

	}
	
	private void setVideoExitValue(int exitValue) {
		switch(exitValue) {
		case 0:
			JOptionPane.showMessageDialog(ImportPanel.this, "Video file imported successfully.");
			break;
		case 1:
			JOptionPane.showMessageDialog(ImportPanel.this, "No directory selected.\n Please select a valid file.");
			break;
		case 2:
			JOptionPane.showMessageDialog(ImportPanel.this, "Invalid file selected.\n Please select a valid file.");
			break;
		default:
			break;
		}
		
	}

}
