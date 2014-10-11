package vamix;

import java.awt.EventQueue;

import vamix.ui.GUI;

/**
 * Constructs the GUI, referencing ComponentManager to create
 * swing components. Utilises swing worker to ensure a responsive
 * GUI.
 */
public class GUIBuilder {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}