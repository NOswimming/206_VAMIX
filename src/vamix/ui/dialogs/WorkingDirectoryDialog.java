package vamix.ui.dialogs;

import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import vamix.component.ComponentManager;

/**
 * The GUI component for selecting a working directory.
 * It is shown by pressing the Set Working Directory button on the MainPanel and is also automatically shown when VAMIX is started.
 * It uses the ComponentManger class to check the selected directory and to manage it.
 * 
 * @see #MainPanel #ComponentManger
 * 
 * @author Callum Fitt-Simpson
 */
public class WorkingDirectoryDialog extends JDialog implements ActionListener {

	JTextField textField_FileChooser;
	JButton button_FileChooser;
	JButton btn_SetDirectory;

	private final JFileChooser fileChooser = new JFileChooser();

	/**
	 * Set up the dialog box.
	 */
	public WorkingDirectoryDialog() {
		setTitle("Set Working Directory");
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		JPanel contentPanel = new JPanel();
		setContentPane(contentPanel);

		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.setLayout(new GridLayout(4,1,2,2));

		JLabel lbl_textLine1 = new JLabel("Select Directory to use as your working directory.");
		lbl_textLine1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_textLine1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lbl_textLine1);
		
		JLabel lbl_textLine2 = new JLabel("All file choosers will open in this directory.");
		lbl_textLine2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_textLine2.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lbl_textLine2);
		
		JLabel lbl_textLine3 = new JLabel("All new files will be saved to this directory.");
		lbl_textLine3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_textLine3.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lbl_textLine3);

		JPanel panel_FileChooser = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_FileChooser.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_FileChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPanel.add(panel_FileChooser);

		JLabel lbl_File = new JLabel("Directory:");
		lbl_File.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_FileChooser.add(lbl_File);

		textField_FileChooser = new JTextField();
		textField_FileChooser.setEditable(false);
		panel_FileChooser.add(textField_FileChooser);
		textField_FileChooser.setColumns(20);

		button_FileChooser = new JButton("...");
		panel_FileChooser.add(button_FileChooser);
		button_FileChooser.addActionListener(this);

		btn_SetDirectory = new JButton("Set Directory");
		panel_FileChooser.add(btn_SetDirectory);
		btn_SetDirectory.addActionListener(this);

		pack();
	}

	/**
	 * Check there is a directory selected and then give it to the Component Manager to handle.
	 */
	private void setWorkignDirectory() {
		if (textField_FileChooser.getText().equals("")) {
			JOptionPane.showMessageDialog(null,
					"Please select a valid directory.");
		} else {
			File f = new File(textField_FileChooser.getText());
			if (!f.isDirectory()) {
				JOptionPane.showMessageDialog(null,
						"Please select a valid directory.");
			} else {
				ComponentManager.getInstance().setWorkingDirectory(f);
				File wd = ComponentManager.getInstance().getWorkingDirectory();
				JOptionPane.showMessageDialog(null,
						"Working directory set: " + wd.getAbsolutePath());
				this.setVisible(false);
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == button_FileChooser) {
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textField_FileChooser.setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}

		if (e.getSource() == btn_SetDirectory) {
			setWorkignDirectory();
		}

	}

}
