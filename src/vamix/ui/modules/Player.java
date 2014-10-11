package vamix.ui.modules;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import vamix.component.ComponentManager;
import vamix.component.components.Video;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player extends JPanel implements ActionListener, ChangeListener, MouseListener {

	public Dimension MinimumSize = new Dimension(500, 400);
	public Dimension MaximumSize = new Dimension(2000, 2000);
	public Dimension PreferedSize = new Dimension(600, 450);

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private JPanel panel_Video;
	private Canvas canvas_Video;

	private JSlider slider_Volume;
	private JButton btn_Mute;
	private JButton btn_Play;
	private JButton btn_Pause;
	private JButton btn_Rewind;
	private JButton btn_FastForward;
	private JProgressBar progressBar;

	private ImportDialog dialog_Import;
	
	private boolean playing = false;

	private int volume;

	private float fastforwardRate = (float) 2.00;
	private long skipRate = -100;

	private Timer timerRewind;
	int timerDelayRewind = 100; // milliseconds
	ActionListener taskPerformerRewind = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (mediaPlayerComponent.getMediaPlayer().getPosition() > 0 && playing == true) {
				mediaPlayerComponent.getMediaPlayer().skip(skipRate);
			}
		}
	};
	
	private Timer timerProgressBar;
	long longToIntDivisor = 100;
	int timerDelayProgressBar = 300; // milliseconds
	ActionListener taskPerformerUpdateBar= new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (mediaPlayerComponent.getMediaPlayer() != null && playing == true){
				if (progressBar.getMaximum() < 1) {
					progressBar.setMaximum((int)(mediaPlayerComponent.getMediaPlayer().getLength() / longToIntDivisor));
				}
				progressBar.setValue((int)(mediaPlayerComponent.getMediaPlayer().getTime() / longToIntDivisor));
				System.out.println(progressBar.getValue() + " / " + progressBar.getMaximum());
			}
			
		}
	};

	private String file = "";

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

		JPanel panel_VideoControls = new JPanel();
		add(panel_VideoControls, BorderLayout.SOUTH);
		panel_VideoControls.setLayout(new BorderLayout(0, 0));

		JPanel panel_VideoControlsEast = new JPanel();
		panel_VideoControls.add(panel_VideoControlsEast, BorderLayout.EAST);

		JLabel lbl_Volume = new JLabel("Volume");
		lbl_Volume.setFont(new Font("Tahoma", Font.PLAIN, 8));
		panel_VideoControlsEast.add(lbl_Volume);

		slider_Volume = new JSlider();
		slider_Volume.setFont(new Font("Tahoma", Font.PLAIN, 8));
		slider_Volume.setValue(100);
		slider_Volume.setPaintTicks(false);
		slider_Volume.setMinorTickSpacing(25);
		slider_Volume.setMaximum(200);
		slider_Volume.setMajorTickSpacing(100);
		slider_Volume.setMinimumSize(new Dimension(100, 16));
		slider_Volume.setMaximumSize(new Dimension(200, 20));
		slider_Volume.setPreferredSize(new Dimension(100, 16));
		slider_Volume.addChangeListener(this);
		panel_VideoControlsEast.add(slider_Volume);

		setVolume(100);

		btn_Mute = new JButton("Mute");
		btn_Mute.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_Mute.addActionListener(this);
		panel_VideoControlsEast.add(btn_Mute);

		JPanel panel_VideoControlsWest = new JPanel();
		panel_VideoControls.add(panel_VideoControlsWest, BorderLayout.WEST);

		btn_Play = new JButton("Play");
		btn_Play.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_Play.addActionListener(this);
		panel_VideoControlsWest.add(btn_Play);

		btn_Rewind = new JButton("RW");
		btn_Rewind.setForeground(new Color(0, 0, 0));
		btn_Rewind.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_Rewind.addActionListener(this);
		panel_VideoControlsWest.add(btn_Rewind);

		btn_Pause = new JButton("Pause");
		btn_Pause.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_Pause.addActionListener(this);
		panel_VideoControlsWest.add(btn_Pause);

		btn_FastForward = new JButton("FF");
		btn_FastForward.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_FastForward.addActionListener(this);
		panel_VideoControlsWest.add(btn_FastForward);
		
		progressBar = new JProgressBar();
		progressBar.addMouseListener(this);
		progressBar.setMaximum(0);
		panel_VideoControls.add(progressBar, BorderLayout.NORTH);
		

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		panel_Video.add(mediaPlayerComponent, BorderLayout.CENTER);

		timerRewind = new Timer(timerDelayRewind, taskPerformerRewind);
		timerProgressBar = new Timer(timerDelayProgressBar, taskPerformerUpdateBar);
		timerProgressBar.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_Play) {
			ComponentManager cm = ComponentManager.getInstance();
			Video video = cm.getVideo();
			if (video == null || video.getFile() == null) {
				int response = JOptionPane
						.showConfirmDialog(this,
								"No video file to play.\n Would you like to import one?");
				if (response == JOptionPane.YES_OPTION) {
					if (dialog_Import != null) {
						dialog_Import.setVisible(false);
						dialog_Import = null;
					}
					dialog_Import = new ImportDialog();
					dialog_Import.setVisible(true);
				}

			} else {
				if (mediaPlayerComponent.getMediaPlayer().getPosition() > 0) {
					if (timerRewind.isRunning()) {
						timerRewind.stop();
					}
					mediaPlayerComponent.getMediaPlayer().setRate(1);
					mediaPlayerComponent.getMediaPlayer().play();
					playing = true;
				} else {
					String absolutePath = video.getFile().getAbsolutePath();
					mediaPlayerComponent.getMediaPlayer().startMedia(
							absolutePath);
					progressBar.setMaximum((int)(mediaPlayerComponent.getMediaPlayer().getLength() / longToIntDivisor));
					playing = true;
					System.out.println(absolutePath);
				}

			}

		}

		if (e.getSource() == btn_Pause) {
			if (timerRewind.isRunning()) {
				timerRewind.stop();
			} 
			mediaPlayerComponent.getMediaPlayer().setPause(true);
			playing = false;

		}

		if (e.getSource() == btn_FastForward) {
			if (timerRewind.isRunning()) {
				timerRewind.stop();
			}
			mediaPlayerComponent.getMediaPlayer().setRate(fastforwardRate);
			mediaPlayerComponent.getMediaPlayer().play();
			playing = true;
		}

		if (e.getSource() == btn_Rewind) {
			if (timerRewind.isRunning()) {
				mediaPlayerComponent.getMediaPlayer().setPause(true);
				timerRewind.stop();
				playing = false;
			} else {
				mediaPlayerComponent.getMediaPlayer().setPause(true);
				timerRewind.start();
				playing = true;
			}

		}

		if (e.getSource() == btn_Mute) {
			if (mediaPlayerComponent != null) {
				mediaPlayerComponent.getMediaPlayer().mute();
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent ce) {

		if (ce.getSource() == slider_Volume) {
			setVolume(slider_Volume.getValue());
		}

	}

	public void setVolume(int newVolume) {
		volume = newVolume;
		if (mediaPlayerComponent != null) {
			mediaPlayerComponent.getMediaPlayer().setVolume(volume);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		double width = e.getComponent().getBounds().getWidth();
		int value = (int) (progressBar.getMaximum()*x/width);
		progressBar.setValue(value);
		mediaPlayerComponent.getMediaPlayer().setTime(value*longToIntDivisor);
		System.out.print("x: "+x + "width: "+width + "value: "+value + "value*longToIntDivisor: "+(value*longToIntDivisor) + "\n");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
