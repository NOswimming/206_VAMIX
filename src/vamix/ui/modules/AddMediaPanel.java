package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import vamix.component.ComponentManager;

public class AddMediaPanel extends JPanel implements ActionListener {

	private JTextField URL;
	private JTextField fileName;
	private JTextField textField_FileChooser;
	private final JFileChooser fc = new JFileChooser();

	private JRadioButton openSource;

	JButton btnDownload;
	JPanel panel_DisplayDownload;
	JScrollPane scrollPane_CenterLeft;

	public AddMediaPanel () {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_Center = new JPanel();
		add(panel_Center, BorderLayout.CENTER);

		panel_Center.setLayout(new BoxLayout(panel_Center, BoxLayout.X_AXIS));

		panel_DisplayDownload = new JPanel();
		panel_DisplayDownload.setLayout(new BoxLayout(panel_DisplayDownload, BoxLayout.Y_AXIS));
		panel_DisplayDownload.setMinimumSize(new Dimension(350, 400));

		scrollPane_CenterLeft = new JScrollPane(panel_DisplayDownload);
		scrollPane_CenterLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_Center.add(scrollPane_CenterLeft);
		scrollPane_CenterLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel panel_CenterRight = new JPanel();
		panel_CenterRight.setLayout(new BoxLayout(panel_CenterRight, BoxLayout.Y_AXIS));
		panel_CenterRight.setMinimumSize(new Dimension(450, 400));
		panel_CenterRight.setPreferredSize(new Dimension(450, 600));
		panel_Center.add(panel_CenterRight);
		
		// Import Panel for adding files to VAMIX
		JPanel panel_ImportOptions = new ImportPanel();
		panel_CenterRight.add(panel_ImportOptions);
		
		panel_CenterRight.add(Box.createRigidArea(new Dimension(0,5)));

		JPanel panel_NewDownload = new JPanel();
		panel_NewDownload.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_CenterRight.add(panel_NewDownload);
		panel_NewDownload.setLayout(new BoxLayout(panel_NewDownload, BoxLayout.Y_AXIS));

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

		JLabel lbl_URL = new JLabel("URL:",JLabel.TRAILING);
		panel_URL.add(lbl_URL);
		lbl_URL.setLabelFor(URL);

		URL = new JTextField();
		panel_URL.add(URL);
		URL.setColumns(20);

		JPanel panel_Filename = new JPanel();
		panel_Filename.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_NewDownload.add(panel_Filename);
		panel_Filename.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel_Filename.setMinimumSize(dim_NewDownloadPanelsMin);
		panel_Filename.setMaximumSize(dim_NewDownloadPanelsMax);
		panel_Filename.setPreferredSize(dim_NewDownloadPanelsPref);


		JLabel lbl_FileName = new JLabel("File Name:",JLabel.TRAILING);
		panel_Filename.add(lbl_FileName);
		lbl_FileName.setLabelFor(fileName);

		fileName = new JTextField();
		panel_Filename.add(fileName);
		fileName.setColumns(20);

		JPanel panel_OpenSourceRadioBtn = new JPanel();
		panel_OpenSourceRadioBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_NewDownload.add(panel_OpenSourceRadioBtn);
		panel_OpenSourceRadioBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
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

		Component verticalGlue = Box.createVerticalGlue();
		panel_CenterRight.add(verticalGlue);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == btnDownload) {

			if(!openSource.isSelected()) {
				JOptionPane.showMessageDialog(this, "The file you are trying to download must be open source.");
				return;
			}
			if(URL.getText() == null && fileName.getText() == null
					|| URL.getText().equals("") && fileName.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please complete both URL and Filename fields.");
				return;
			}
			DownloadModule d1 = new DownloadModule(URL.getText(), fileName.getText());
			panel_DisplayDownload.add(d1);
			panel_DisplayDownload.revalidate();
			scrollPane_CenterLeft.revalidate();
			validate();
		}


	}

}