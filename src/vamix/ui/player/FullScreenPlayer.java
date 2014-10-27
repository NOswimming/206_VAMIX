package vamix.ui.player;

import javax.swing.JFrame;

//TODO: remove this class if full screen is not implemented
public class FullScreenPlayer extends JFrame {

	public FullScreenPlayer() {
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(false);
	}

	public void showFullScreen(Player player) {
		setContentPane(player);
		revalidate();
		toFront();
		setVisible(true);
	}

	public void hideFullScreen() {
		setVisible(false);
	}

}
