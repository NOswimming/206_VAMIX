package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import vamix.misc.Helper;

import javax.swing.JTextPane;
import javax.swing.JTextArea;

/**
 * Adds text to the start of the video,
 */
public class AddTextDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	private JButton btn_FileChooser;
	private JButton btn_AddText;
	private JButton btn_CancelClose;

	final JFileChooser font_select = new JFileChooser();
	JTextField fontSelectText = new JTextField("Select your font");
	private JTextPane textPane;
	private JTextArea textArea;
	private JProgressBar progressBar;
	
	private boolean finished = false;
	
	private int durationFrames;
	private int durationSeconds;
	private int fps;

	public enum Type {
		START, END
	};

	public AddTextDialog(Type type) {

		setTitle("Add Text to the " + type.toString().toLowerCase()
				+ "start of the Video");

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		DocumentFilter publicNotesfilter = new LimitedCharactersDocumentFilter(150);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.ITALIC, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLUE);
		((PlainDocument) textArea.getDocument()).setDocumentFilter(publicNotesfilter);
		textArea.setMinimumSize(new Dimension(400,100));
		textArea.setMaximumSize(new Dimension(2000,1000));
		textArea.setPreferredSize(new Dimension(400,100));
		
		contentPanel.add(textArea);
		
		Dimension dim_PanelsMin = new Dimension(400, 35);
		Dimension dim_PanelsMax = new Dimension(2000, 40);
		Dimension dim_PanelsPref = new Dimension(400, 35);
		
		JPanel panel_SelectFont = new JPanel();
		panel_SelectFont.setMinimumSize(dim_PanelsMin);
		panel_SelectFont.setMaximumSize(dim_PanelsMax);
		panel_SelectFont.setPreferredSize(dim_PanelsPref);
		panel_SelectFont.setLayout(new FlowLayout());
		FlowLayout flowLayout2 = (FlowLayout) panel_SelectFont.getLayout();
		flowLayout2.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_SelectFont);

		font_select.setCurrentDirectory(new File("/usr/share/fonts/truetype/"));
		font_select.setDialogTitle("Select a font file");
		
		fontSelectText.setColumns(20);
		fontSelectText.setEditable(false);
		panel_SelectFont.add(fontSelectText);
		
		btn_FileChooser = new JButton("...");
		btn_FileChooser.addActionListener(this);
		panel_SelectFont.add(btn_FileChooser);
		
		Component verticalGlue = Box.createVerticalGlue();
		contentPanel.add(verticalGlue);

		// Button panel

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btn_AddText = new JButton("Add Text");
		btn_AddText.setActionCommand("Add Text");
		btn_AddText.addActionListener(this);
		buttonPane.add(btn_AddText);
		getRootPane().setDefaultButton(btn_AddText);

		btn_CancelClose = new JButton("Close");
		btn_CancelClose.setActionCommand("Close");
		btn_CancelClose.addActionListener(this);
		buttonPane.add(btn_CancelClose);

		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == btn_FileChooser) {
			
			int returnVal = font_select.showOpenDialog(AddTextDialog.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File f = new File(font_select.getSelectedFile()
						.getAbsolutePath());
				if (Helper.checkType(f).equals("application/x-font-ttf")) {
					fontSelectText.setText(font_select.getSelectedFile()
							.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(AddTextDialog.this,
							"Please select a font file.");
				}
			}
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
