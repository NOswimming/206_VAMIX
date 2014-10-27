package vamix.ui.components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.CompoundBorder;

import vamix.function.DownloadFunction;
import vamix.function.worker.Worker;

public class DownloadModule extends JPanel {

	public Dimension MinimumSize = new Dimension(300, 90);
	public Dimension MaximumSize = new Dimension(2000, 90);
	public Dimension PreferedSize = new Dimension(300, 90);

	private String name;

	private JProgressBar progressBar;
	private JButton btn_cancelOkay;

	/**
	 * If the download is finished and waiting for removal
	 */
	private boolean finished = false;

	private DownloadFunction download;

	/**
	 * Create the panel.
	 */
	public DownloadModule(String URL, String filename) {
		name = filename;
		setMinimumSize(MinimumSize);
		setMaximumSize(MaximumSize);
		setPreferredSize(PreferedSize);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border empty = BorderFactory.createEmptyBorder(5, 10, 5, 10);
		Border compound = new CompoundBorder(empty, blackline);
		setBorder(new CompoundBorder(compound, empty));

		JLabel lbl_Filename = new JLabel(filename);
		add(lbl_Filename);

		JLabel lbl_URL = new JLabel(URL);
		add(lbl_URL);

		progressBar = new JProgressBar();
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(progressBar);

		btn_cancelOkay = new JButton("Cancel");
		add(btn_cancelOkay);
		btn_cancelOkay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!finished) {
					download.cancelDl();
				}
				remove();
			}
		});
		download = new DownloadFunction(URL, filename, this);
	}

	public void setProgress(int progress) {
		progressBar.setValue(progress);
	}

	public void done(int exitValue) {
		switch (exitValue) {
		case 1:
			JOptionPane
					.showMessageDialog(
							this,
							name
									+ " Failed: Error code 1, cause unknown, general error, sorry.");
			break;
		case 2:
			JOptionPane.showMessageDialog(this, name + " Failed: Parse Error");
			break;
		case 3:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: File I/O Error");
			break;
		case 4:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: Network failure (Probably just Uni WiFi");
			break;
		case 5:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: SSL Verification failure");
			break;
		case 6:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: Username/Password Authentication failure");
			break;
		case 8:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: Server issued an error response");
			break;
		case 7:
			JOptionPane.showMessageDialog(this, name
					+ " Failed: Protocol Errors");
			break;
		case Worker.CANCELED_EXIT_VALUE:
			JOptionPane.showMessageDialog(this,
					" Downloading has been cancelled.");
		default:
			break;
		}
		finished = true;
		btn_cancelOkay.setText("Okay");
	}

	private void remove() {
		Container c = this.getParent();
		c.remove(this);
		((JComponent) c).revalidate();
		c.repaint();
	}

}
