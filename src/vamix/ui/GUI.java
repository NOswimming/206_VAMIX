package vamix.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.DarkStar;

import vamix.ui.modules.MainPanel;

public class GUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GUI() {
		this.setTitle("VAMIX - Video and Audio Mixer");
		
            try {
            	
            	PlasticLookAndFeel.setPlasticTheme(new DarkStar());
				UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(900, 600));
		setPreferredSize(new Dimension(900, 600));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		/// Editor panel
		JPanel panel_Main = new MainPanel();
		contentPane.add(panel_Main, BorderLayout.CENTER);
		
	}
	
}