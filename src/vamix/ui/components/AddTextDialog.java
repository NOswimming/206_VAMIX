package vamix.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import vamix.function.DrawTextFunction;
import vamix.function.ExtractAudioFunction;
import vamix.misc.Helper;

import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.GridLayout;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;

/**
 * Adds text to the start of the video,
 */
public class AddTextDialog extends JDialog implements ActionListener,
		ChangeListener, ItemListener {

	private final JPanel contentPanel = new JPanel();

	private JButton btn_FileChooser;
	private JButton btn_AddText;
	private JButton btn_CancelClose;

	final JFileChooser font_select = new JFileChooser();
	private JTextField fontSelectText;
	private JTextArea textArea;
	private JProgressBar progressBar;
	private JPanel panel_FontOptions;
	private JPanel panel_FontColor;
	private JLabel lblFontSize;
	private JComboBox comboBox_FontSize;
	private JPanel panel_FontSize;
	private JLabel lblFontColor;
	private JButton btn_ColorChooser;
	private JPanel panel_ColorSample;
	private JColorChooser colorChooser;
	private JDialog dialog_ColorChooser;
	private JLabel lblTextToBeAdded;
	private ImportDialog dialog_Import;

	public static final String[] fontSizes = new String[] { "6", "8", "10",
			"12", "14", "16", "18", "20", "24", "28", "32", "36", "40", "44",
			"48", "54", "60", "72", "90", "100" };

	private int fontSize = 48;
	private File fontFile = new File(
			"/usr/share/fonts/truetype/ttf-indic-fonts-core/gargi.ttf");
	private Font font = new Font("Serif", Font.PLAIN, fontSize);
	private Color selectedColor = Color.WHITE;

	private boolean finished = true;

	private DrawTextFunction drawText;

	private int durationFrames;
	private int durationSeconds;
	private int fps;

	private String outputName = "drawTextTest";
	private String outputDirectory = "";

	public enum Type {
		START, END
	};

	public AddTextDialog(Type type) {

		setTitle("Add Text to the " + type.toString().toLowerCase()
				+ " of the Video");

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		lblTextToBeAdded = new JLabel("Text to be added");
		lblTextToBeAdded.setHorizontalAlignment(SwingConstants.LEFT);
		lblTextToBeAdded.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(lblTextToBeAdded);

		DocumentFilter limitedCharactersfilter = new LimitedCharactersDocumentFilter(
				150);

		textArea = new JTextArea();
		textArea.setFont(font);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(selectedColor);
		textArea.setBackground(Color.LIGHT_GRAY);
		((PlainDocument) textArea.getDocument())
				.setDocumentFilter(limitedCharactersfilter);
		textArea.setMinimumSize(new Dimension(600, 300));
		textArea.setMaximumSize(new Dimension(2000, 1000));
		textArea.setPreferredSize(new Dimension(600, 300));

		contentPanel.add(textArea);

		Dimension dim_PanelsMin = new Dimension(400, 35);
		Dimension dim_PanelsMax = new Dimension(2000, 40);
		Dimension dim_PanelsPref = new Dimension(400, 35);

		panel_FontOptions = new JPanel();
		contentPanel.add(panel_FontOptions);
		panel_FontOptions.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel panel_SelectFont = new JPanel();
		panel_FontOptions.add(panel_SelectFont);

		fontSelectText = new JTextField("Select your font");
		fontSelectText.setColumns(20);
		fontSelectText.setEditable(false);
		panel_SelectFont.add(fontSelectText);

		btn_FileChooser = new JButton("...");
		btn_FileChooser.addActionListener(this);
		panel_SelectFont.add(btn_FileChooser);

		font_select.setCurrentDirectory(new File("/usr/share/fonts/truetype/"));
		font_select.setDialogTitle("Select a font file");

		panel_FontSize = new JPanel();
		panel_FontOptions.add(panel_FontSize);

		lblFontSize = new JLabel("Font Size");
		panel_FontSize.add(lblFontSize);

		comboBox_FontSize = new JComboBox();
		panel_FontSize.add(comboBox_FontSize);
		comboBox_FontSize.setModel(new DefaultComboBoxModel(fontSizes));
		comboBox_FontSize.setSelectedIndex(14);
		comboBox_FontSize.addItemListener(this);

		panel_FontColor = new JPanel();
		panel_FontOptions.add(panel_FontColor);

		lblFontColor = new JLabel("Font Color");
		panel_FontColor.add(lblFontColor);

		panel_ColorSample = new JPanel();
		panel_ColorSample.setBackground(selectedColor);
		panel_ColorSample.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_FontColor.add(panel_ColorSample);

		btn_ColorChooser = new JButton("...");
		btn_ColorChooser.addActionListener(this);
		panel_FontColor.add(btn_ColorChooser);

		progressBar = new JProgressBar();
		contentPanel.add(progressBar);

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

		// Color chooser dialog

		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);

		JButton btn_CloseColorChooser = new JButton("Choose Colour");

		dialog_ColorChooser = new JDialog();
		dialog_ColorChooser.setTitle("Choose Font Color");
		dialog_ColorChooser.getContentPane().setLayout(new BorderLayout());
		dialog_ColorChooser.getContentPane().add(colorChooser,
				BorderLayout.CENTER);
		dialog_ColorChooser.getContentPane().add(btn_CloseColorChooser,
				BorderLayout.SOUTH);
		btn_CloseColorChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog_ColorChooser.setVisible(false);
			}
		});

		dialog_ColorChooser.pack();
		dialog_ColorChooser.setVisible(false);

		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == btn_AddText) {
			String text = textArea.getText();

			if (text == null || text.equals("")) {
				JOptionPane.showMessageDialog(this,
						"Please enter some text to overlay.");
				return;
			}
			drawText = new DrawTextFunction(this);
			int result = drawText.canDrawText(outputName, outputDirectory,
					text, toHexString(selectedColor), "" + fontSize,
					fontFile.getAbsolutePath());
			canDrawTextExitValue(result);
		}

		if (e.getSource() == btn_FileChooser) {
			int returnVal = font_select.showOpenDialog(AddTextDialog.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// check the selected file is a ttf (True type font) file
				File selectedFont = font_select.getSelectedFile();
				if (selectedFont.getName().matches("(.*)\\.ttf")) {
					try {
						fontFile = font_select.getSelectedFile();
						font = Font.createFont(Font.TRUETYPE_FONT,
								new FileInputStream(fontFile));
						GraphicsEnvironment.getLocalGraphicsEnvironment()
								.registerFont(font);
						font = font.deriveFont((float) fontSize);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FontFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				fontSelectText.setText(font.getName());
				textArea.setFont(font);
				textArea.repaint();
			}

		}

		if (e.getSource() == btn_ColorChooser) {
			dialog_ColorChooser.setVisible(true);
		}

		if (e.getSource() == btn_CancelClose) {
			if (!finished) {
				System.out.println("cancelAt()");
				drawText.cancelDt();
			} else {
				dispose();
			}
		}
	}

	@Override
	// Code from the Oracle Tutorials site for updating the JFileChooser
	public void stateChanged(ChangeEvent e) {
		selectedColor = colorChooser.getColor();

		panel_ColorSample.setBackground(selectedColor);
		panel_ColorSample.repaint();
		textArea.setForeground(selectedColor);
		textArea.repaint();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		fontSize = Integer.parseInt(comboBox_FontSize.getSelectedItem()
				.toString());
		font = font.deriveFont((float) fontSize);
		textArea.setFont(font);
		textArea.repaint();

	}

	public void canDrawTextExitValue(int exitValue) {
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
			JOptionPane
					.showMessageDialog(this,
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
	 * 
	 * @param i
	 */
	public void setProgress(int i) {
		progressBar.setValue(i);
	}

	/**
	 * Sets the number of frames per second of the video file being stripped
	 * 
	 * @param seconds
	 */
	public void setFps(int fps) {
		this.fps = fps;
		setDurationFrames();
	}

	/**
	 * Sets the duration of the video file being stripped in seconds
	 * 
	 * @param seconds
	 */
	public void setDurationSeconds(int seconds) {
		durationSeconds = seconds;
		setDurationFrames();
	}

	/**
	 * Sets the duration of the video file being stripped in frames and then
	 * updates the progress bar if it can.
	 */
	private void setDurationFrames() {
		if (durationSeconds > 0 && fps > 0) {
			durationFrames = durationSeconds * fps;
			progressBar.setMaximum(durationFrames);
		}
	}

	// Taken from http://www.javacreed.com/how-to-get-the-hex-value-from-color/
	// Tutorial
	public final static String toHexString(Color colour)
			throws NullPointerException {
		String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		if (hexColour.length() < 6) {
			hexColour = "000000".substring(0, 6 - hexColour.length())
					+ hexColour;
		}
		return "0x" + hexColour;
	}

}