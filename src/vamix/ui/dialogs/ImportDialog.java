package vamix.ui.dialogs;

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
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import vamix.component.ComponentManager;

/**
 * The GUI component for Importing files to play with the player.
 * It is shown by pressing the Import button on the MainPanel or by pressing the + button on the player.
 * It uses the ComponentManger class to check the files are valid audio or video files before importing them.
 * 
 * @see #MainPanel #Player #ComponentManger
 * 
 * @author Callum Fitt-Simpson
 */
public class ImportDialog extends JDialog implements ActionListener {

	JTextField textField_FileChooser;
	JButton button_FileChooser;
	JButton btn_Import;

	List<ActionListener> confirmListeners = new LinkedList<ActionListener>();

	private final JFileChooser fileChooser = new JFileChooser();

	/**
	 * Set up the dialog.
	 */
	public ImportDialog() {
		setTitle("Import File");

		JPanel contentPanel = new JPanel();
		setContentPane(contentPanel);

		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JLabel lbl_Import = new JLabel("Import a Video or Audio File");
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

	/**
	 * Update the user on whether or not importing the file was successful.
	 */
	private void importExitValue(boolean exitValue) {
		if (exitValue) {
			JOptionPane.showMessageDialog(null,
					"File imported successfully.");
			this.setVisible(false);
			updateConfirmListeners();
		} else {
			JOptionPane.showMessageDialog(null,
					"Invalid file selected.\n Not a video or audio file.\n Please select a valid file.");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == button_FileChooser) {
			fileChooser.setCurrentDirectory(ComponentManager.getInstance().getWorkingDirectory());
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textField_FileChooser.setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}

		if (e.getSource() == btn_Import) {
			if (textField_FileChooser.getText().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please select a video file to import.");
			} else {
				boolean result = ComponentManager.getInstance().importFile(textField_FileChooser.getText());
				importExitValue(result);
			}
		}

	}

	/**
	 * For updating the Player when importing is complete.
	 */
	public void addConfirmListener(ActionListener listener) {
		confirmListeners.add(listener);
	}

	/**
	 * For updating the Player when importing is complete.
	 */
	public void updateConfirmListeners() {
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				"confirm");
		for (ActionListener al : confirmListeners) {
			al.actionPerformed(e);
		}
	}

}
