package vamix.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.GridLayout;

/**
 * The GUI component for Downloading files.
 * It is shown by pressing the Download Manager button on the MainPanel.
 * It adds DownloadModule components to the scroll pane, 
 * which in turn call the DownloadFunction to run the wget process.
 * 
 * @see #MainPanel #DownloadModule #DownloadFunction
 * 
 * @author Callum Fitt-Simpson
 */
public class DownloadManagerFrame extends JFrame implements ActionListener {

	private JTextField textField_URL;
	private JTextField textField_fileName;

	private JRadioButton openSource;

	JButton btnDownload;
	JPanel panel_DisplayDownload;
	JScrollPane scrollPane;

	Container contentPane;

	public DownloadManagerFrame() {
		setTitle("Download Manager");

		this.setMinimumSize(new Dimension(400, 350));

		contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout(0, 0));

		// Scroll pane for displaying the downloads

		panel_DisplayDownload = new JPanel();
		panel_DisplayDownload.setLayout(new BoxLayout(panel_DisplayDownload,
				BoxLayout.Y_AXIS));
		panel_DisplayDownload.setMinimumSize(new Dimension(400, 200));

		scrollPane = new JScrollPane(panel_DisplayDownload);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension(400, 200));
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// New Download panel

		JPanel panel_NewDownload = new JPanel();
		panel_NewDownload.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_NewDownload.setLayout(new GridLayout(5, 1, 0, 0));
		panel_NewDownload.setPreferredSize(new Dimension(400, 180));
		contentPane.add(panel_NewDownload, BorderLayout.NORTH);

		JLabel label_NewDownload = new JLabel("New Download");
		label_NewDownload.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_NewDownload.setHorizontalAlignment(SwingConstants.CENTER);
		panel_NewDownload.add(label_NewDownload);

		Dimension dim_NewDownloadPanelsMin = new Dimension(400, 35);
		Dimension dim_NewDownloadPanelsMax = new Dimension(2000, 40);
		Dimension dim_NewDownloadPanelsPref = new Dimension(400, 35);

		JPanel panel_URL = new JPanel();
		panel_URL.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_NewDownload.add(panel_URL);
		panel_URL.setBorder(null);
		panel_URL.setMinimumSize(dim_NewDownloadPanelsMin);
		panel_URL.setMaximumSize(dim_NewDownloadPanelsMax);
		panel_URL.setPreferredSize(dim_NewDownloadPanelsPref);
		panel_URL.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lbl_URL = new JLabel("URL:", JLabel.TRAILING);
		lbl_URL.setLabelFor(textField_URL);
		panel_URL.add(lbl_URL);

		textField_URL = new JTextField();
		textField_URL.setColumns(30);
		panel_URL.add(textField_URL);

		JPanel panel_OpenSourceRadioBtn = new JPanel();
		panel_OpenSourceRadioBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_NewDownload.add(panel_OpenSourceRadioBtn);
		panel_OpenSourceRadioBtn
				.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel_OpenSourceRadioBtn.setMinimumSize(dim_NewDownloadPanelsMin);
		panel_OpenSourceRadioBtn.setMaximumSize(dim_NewDownloadPanelsMax);
		panel_OpenSourceRadioBtn.setPreferredSize(dim_NewDownloadPanelsPref);

		openSource = new JRadioButton("Open Source?");
		panel_OpenSourceRadioBtn.add(openSource);

		JPanel panel_DownloadBtn = new JPanel();
		panel_DownloadBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_NewDownload.add(panel_DownloadBtn);
		panel_DownloadBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel_DownloadBtn.setMinimumSize(dim_NewDownloadPanelsMin);
		panel_DownloadBtn.setMaximumSize(dim_NewDownloadPanelsMax);
		panel_DownloadBtn.setPreferredSize(dim_NewDownloadPanelsPref);

		btnDownload = new JButton("Download");
		panel_DownloadBtn.add(btnDownload);
		btnDownload.addActionListener(this);

		pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnDownload) {

			if (!openSource.isSelected()) {
				JOptionPane
						.showMessageDialog(this,
								"The file you are trying to download must be open source.");
				return;
			}
			if (textField_URL.getText() == null || textField_URL.getText().equals("")) {
				JOptionPane.showMessageDialog(this,
						"Please complete the URL field.");
				return;
			}
			DownloadModule d1 = new DownloadModule(textField_URL.getText());
			panel_DisplayDownload.add(d1);
			panel_DisplayDownload.revalidate();
			scrollPane.revalidate();
			validate();
		}

	}

}
