package vamix.ui.player;

import vamix.component.ComponentManager;
import vamix.component.components.Video;
import vamix.ui.GUI;
import vamix.ui.dialogs.ImportDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

public class Player extends JPanel implements ActionListener, ChangeListener,
		MouseListener, MediaPlayerEventListener {

	public Dimension MinimumSize = new Dimension(600, 400);
	public Dimension MaximumSize = new Dimension(2000, 2000);
	public Dimension PreferedSize = new Dimension(600, 400);

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private JPanel panel_Video;

	private JProgressBar progressBar;

	private JButton btn_Add;
	private JButton btn_Stop;
	private JButton btn_PlayPause;
	private JButton btn_Rewind;
	private JButton btn_FastForward;

	private JLabel lbl_Volume;
	private JSlider slider_Volume;
	private JButton btn_Mute;
	// private JButton btn_FullScreen; TODO: remove

	public static final Color BUTTON_COLOR = new Color(100, 100, 100, 255);

	// Icons for all the buttons
	private ImageIcon playIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/PlayButtonIcon.png"));
	private ImageIcon pauseIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/PauseButtonIcon.png"));
	private ImageIcon stopIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/StopButtonIcon.png"));
	private ImageIcon fastForwardIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/FastForwardButtonIcon.png"));
	private ImageIcon rewindIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/RewindButtonIcon.png"));
	private ImageIcon mutedIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/MutedButtonIcon.png"));
	private ImageIcon unmutedIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/UnmutedButtonIcon.png"));
	private ImageIcon addIcon = new ImageIcon(
			GUI.class.getResource("/vamix/ui/player/icons/AddButtonIcon.png"));

	private ImportDialog dialog_Import;

	private boolean playing = false;

	private int volume;

	private float fastforwardRate = (float) 2.00;
	private long skipRate = -100;

	private Timer timerRewind;
	int timerDelayRewind = 100; // milliseconds
	ActionListener taskPerformerRewind = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (playing == true) {
				mediaPlayerComponent.getMediaPlayer().skip(skipRate);
			}
		}
	};

	private Timer timerProgressBar;
	long longToIntDivisor = 100;
	int timerDelayProgressBar = 300; // milliseconds
	ActionListener taskPerformerUpdateBar = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (playing == true) {
				if (progressBar.getMaximum() < 1) {
					Player.this.setProgressBarMaximum(mediaPlayerComponent
							.getMediaPlayer().getLength());
				}
				Player.this.updateProgressBar(mediaPlayerComponent
						.getMediaPlayer().getTime());
			}

		}
	};

	/**
	 * Create the panel.
	 */
	public Player() {

		setMinimumSize(MinimumSize);
		setMaximumSize(MaximumSize);
		setPreferredSize(PreferedSize);

		setLayout(new BorderLayout(0, 0));

		panel_Video = new JPanel();
		panel_Video.setLayout(new BorderLayout());
		panel_Video.setBackground(Color.DARK_GRAY);
		add(panel_Video, BorderLayout.CENTER);

		// Media Player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(this);
		panel_Video.add(mediaPlayerComponent, BorderLayout.CENTER);

		// Video Controls
		JPanel panel_VideoControls = new JPanel();
		add(panel_VideoControls, BorderLayout.SOUTH);
		panel_VideoControls.setLayout(new BorderLayout(10, 0));

		JPanel panel_VideoControlsEast = new JPanel();
		panel_VideoControls.add(panel_VideoControlsEast, BorderLayout.EAST);

		lbl_Volume = new JLabel("Volume");
		lbl_Volume.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_Volume.setEnabled(false);
		panel_VideoControlsEast.add(lbl_Volume);

		slider_Volume = new JSlider();
		slider_Volume.setValue(100);
		slider_Volume.setPaintTicks(false);
		slider_Volume.setMinorTickSpacing(25);
		slider_Volume.setMaximum(200);
		slider_Volume.setMajorTickSpacing(100);
		slider_Volume.setMinimumSize(new Dimension(100, 16));
		slider_Volume.setMaximumSize(new Dimension(200, 20));
		slider_Volume.setPreferredSize(new Dimension(100, 16));
		slider_Volume.addChangeListener(this);
		slider_Volume.setEnabled(false);
		panel_VideoControlsEast.add(slider_Volume);

		setVolume(100);

		btn_Mute = new JButton();
		btn_Mute.setIcon(unmutedIcon);
		btn_Mute.setBackground(BUTTON_COLOR);
		btn_Mute.setPreferredSize(new Dimension(30, 30));
		btn_Mute.addActionListener(this);
		btn_Mute.setEnabled(false);
		panel_VideoControlsEast.add(btn_Mute);

		/*
		 * // TODO: remove btn_FullScreen = new JButton("FS");
		 * btn_FullScreen.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 * btn_FullScreen.addActionListener(this);
		 * btn_FullScreen.setEnabled(false);
		 * panel_VideoControlsEast.add(btn_FullScreen);
		 */

		JPanel panel_VideoControlsWest = new JPanel();
		panel_VideoControls.add(panel_VideoControlsWest, BorderLayout.WEST);

		btn_Add = new JButton();
		btn_Add.setIcon(addIcon);
		btn_Add.setBackground(BUTTON_COLOR);
		btn_Add.setPreferredSize(new Dimension(30, 30));
		btn_Add.addActionListener(this);
		panel_VideoControlsWest.add(btn_Add);

		btn_Stop = new JButton();
		btn_Stop.setIcon(stopIcon);
		btn_Stop.setBackground(BUTTON_COLOR);
		btn_Stop.setPreferredSize(new Dimension(30, 30));
		btn_Stop.addActionListener(this);
		btn_Stop.setEnabled(false);
		panel_VideoControlsWest.add(btn_Stop);

		btn_Rewind = new JButton();
		btn_Rewind.setIcon(rewindIcon);
		btn_Rewind.setBackground(BUTTON_COLOR);
		btn_Rewind.setPreferredSize(new Dimension(30, 30));
		btn_Rewind.addActionListener(this);
		btn_Rewind.setEnabled(false);
		panel_VideoControlsWest.add(btn_Rewind);

		btn_PlayPause = new JButton();
		btn_PlayPause.setIcon(playIcon);
		btn_PlayPause.setBackground(BUTTON_COLOR);
		btn_PlayPause.setPreferredSize(new Dimension(30, 30));
		btn_PlayPause.addActionListener(this);
		btn_PlayPause.setEnabled(false);
		panel_VideoControlsWest.add(btn_PlayPause);

		btn_FastForward = new JButton();
		btn_FastForward.setIcon(fastForwardIcon);
		btn_FastForward.setBackground(BUTTON_COLOR);
		btn_FastForward.setPreferredSize(new Dimension(30, 30));
		btn_FastForward.addActionListener(this);
		btn_FastForward.setEnabled(false);
		panel_VideoControlsWest.add(btn_FastForward);

		progressBar = new JProgressBar();
		progressBar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		progressBar.addMouseListener(this);
		progressBar.setMaximum(0);
		progressBar.setStringPainted(true);
		progressBar.setString(" ");
		panel_VideoControls.add(progressBar, BorderLayout.NORTH);

		timerRewind = new Timer(timerDelayRewind, taskPerformerRewind);
		timerProgressBar = new Timer(timerDelayProgressBar,
				taskPerformerUpdateBar);
		timerProgressBar.start();

		dialog_Import = new ImportDialog();

	}

	// ActionListener Events

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_Add) {
			ComponentManager cm = ComponentManager.getInstance();
			File file = cm.getPlayFile();
			if (file == null || file.getAbsolutePath() == null) {
				int response = JOptionPane
						.showConfirmDialog(this,
								"No video file to play.\n Would you like to import one?");
				if (response == JOptionPane.YES_OPTION) {
					dialog_Import.setVisible(true);
					dialog_Import.addConfirmListener(this);
				}

			} else {
				String absolutePath = file.getAbsolutePath();
				mediaPlayerComponent.getMediaPlayer().startMedia(absolutePath);
				setProgressBarMaximum(mediaPlayerComponent.getMediaPlayer()
						.getLength());
				playing(true);
				enableButtons(true);
			}
		}

		if (e.getSource() == dialog_Import) {
			ComponentManager cm = ComponentManager.getInstance();
			File file = cm.getPlayFile();

			String absolutePath = file.getAbsolutePath();
			mediaPlayerComponent.getMediaPlayer().startMedia(absolutePath);
			setProgressBarMaximum(mediaPlayerComponent.getMediaPlayer()
					.getLength());
			playing(true);
			enableButtons(true);
		}

		if (e.getSource() == btn_Stop) {
			if (timerRewind.isRunning()) {
				timerRewind.stop();
			}
			mediaPlayerComponent.getMediaPlayer().setRate(1);
			mediaPlayerComponent.getMediaPlayer().stop();
			updateProgressBar(0);
			playing(false);

		}

		if (e.getSource() == btn_PlayPause) {
			if (timerRewind.isRunning()) {
				timerRewind.stop();
			}
			mediaPlayerComponent.getMediaPlayer().setRate(1);
			if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
				mediaPlayerComponent.getMediaPlayer().pause();
				playing(false);
			} else {
				mediaPlayerComponent.getMediaPlayer().play();
				playing(true);
			}
		}

		if (e.getSource() == btn_FastForward) {
			if (timerRewind.isRunning()) {
				timerRewind.stop();
			}
			mediaPlayerComponent.getMediaPlayer().setRate(fastforwardRate);
			mediaPlayerComponent.getMediaPlayer().play();
			playing(true);
		}

		if (e.getSource() == btn_Rewind) {
			if (timerRewind.isRunning()) {
				mediaPlayerComponent.getMediaPlayer().setPause(true);
				timerRewind.stop();
				playing(false);
			} else {
				mediaPlayerComponent.getMediaPlayer().setPause(true);
				timerRewind.start();
				playing(true);
			}

		}

		if (e.getSource() == btn_Mute) {
			if (mediaPlayerComponent != null) {
				if (mediaPlayerComponent.getMediaPlayer().isMute())
					btn_Mute.setIcon(unmutedIcon);
				else
					btn_Mute.setIcon(mutedIcon);
				mediaPlayerComponent.getMediaPlayer().mute();
			}
		}

		/*
		 * // TODO: remove if (e.getSource() == btn_FullScreen) { if
		 * (mediaPlayerComponent != null) {
		 * mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
		 * System.out.println("fullscreen"); } }
		 */

	}

	// ChangeListener Event

	@Override
	public void stateChanged(ChangeEvent ce) {

		if (ce.getSource() == slider_Volume) {
			setVolume(slider_Volume.getValue());
		}

	}

	public void playing(boolean isPlaying) {
		if (isPlaying) {
			btn_PlayPause.setIcon(pauseIcon);
			playing = true;
		} else {
			btn_PlayPause.setIcon(playIcon);
			playing = false;
		}
	}

	public void enableButtons(boolean enable) {
		lbl_Volume.setEnabled(enable);
		slider_Volume.setEnabled(enable);
		btn_Mute.setEnabled(enable);
		// btn_FullScreen.setEnabled(enable); // TODO: remove
		btn_Stop.setEnabled(enable);
		btn_Rewind.setEnabled(enable);
		btn_PlayPause.setEnabled(enable);
		btn_FastForward.setEnabled(enable);

	}

	public void setProgressBarMaximum(long milliseconds) {
		progressBar.setMaximum((int) (milliseconds / longToIntDivisor));
	}

	public void updateProgressBar(long milliseconds) {
		progressBar.setValue((int) (milliseconds / longToIntDivisor));
		long second = (milliseconds / 1000) % 60;
		long minute = (milliseconds / (1000 * 60)) % 60;
		long hour = (milliseconds / (1000 * 60 * 60)) % 24;
		String formattedTime = String.format("%01d:%02d:%02d", hour, minute,
				second);
		progressBar.setString(formattedTime);
	}

	public void setVolume(int newVolume) {
		volume = newVolume;
		if (mediaPlayerComponent != null) {
			mediaPlayerComponent.getMediaPlayer().setVolume(volume);
		}
	}

	// MouseListener Events

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == progressBar) {
			int x = e.getX();
			double width = e.getComponent().getBounds().getWidth();
			long value = (long) (progressBar.getMaximum() * x / width);
			updateProgressBar(value);
			mediaPlayerComponent.getMediaPlayer().setTime(
					value * longToIntDivisor);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	// MediaPlayerEventListener Events

	@Override
	public void backward(MediaPlayer arg0) {
	}

	@Override
	public void buffering(MediaPlayer arg0, float arg1) {
	}

	@Override
	public void endOfSubItems(MediaPlayer arg0) {
	}

	@Override
	public void error(MediaPlayer arg0) {
	}

	@Override
	public void finished(MediaPlayer arg0) {
		if (timerRewind.isRunning()) {
			timerRewind.stop();
		}
		mediaPlayerComponent.getMediaPlayer().setRate(1);
		mediaPlayerComponent.getMediaPlayer().stop();
		updateProgressBar(0);
		playing(false);
	}

	@Override
	public void forward(MediaPlayer arg0) {
	}

	@Override
	public void lengthChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
	}

	@Override
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void mediaFreed(MediaPlayer arg0) {
	}

	@Override
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
	}

	@Override
	public void newMedia(MediaPlayer arg0) {
	}

	@Override
	public void opening(MediaPlayer arg0) {
	}

	@Override
	public void pausableChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void paused(MediaPlayer arg0) {
	}

	@Override
	public void playing(MediaPlayer arg0) {
	}

	@Override
	public void positionChanged(MediaPlayer arg0, float arg1) {
	}

	@Override
	public void seekableChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void snapshotTaken(MediaPlayer arg0, String arg1) {
	}

	@Override
	public void stopped(MediaPlayer arg0) {
	}

	@Override
	public void subItemFinished(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void subItemPlayed(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void timeChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void titleChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void videoOutput(MediaPlayer arg0, int arg1) {
	}

}
