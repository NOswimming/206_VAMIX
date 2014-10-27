package vamix;

import java.awt.EventQueue;

import vamix.ui.GUI;

/**
 * Runs the entire VAMIX application.
 * Constructs the GUI through the GUI class.
 * Utilises swing worker to ensure a responsive GUI.
 * 
 * @see #GUI
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public class VAMIXmain {

	/**
	 * Launch the VAMIX application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI gui = GUI.getInstance();
					gui.setUpGUI();
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}