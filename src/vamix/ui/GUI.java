package vamix.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollBar;

import vamix.ui.modules.AddMediaPanel;
import vamix.ui.modules.EditorPanel;
import vamix.ui.modules.ExportPanel;

public class GUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GUI() {
		this.setTitle("VAMIX - Video and Audio Mixer");
		
		Border borderBlackLine = BorderFactory.createLineBorder(Color.black);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		setPreferredSize(new Dimension(800, 600));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		// AddMedia panel
		JPanel tabPanel_AddMedia = new AddMediaPanel();
		tabbedPane.addTab("Add Media", null, tabPanel_AddMedia, null);
		
		/// Editor panel
		JPanel tabPanel_Editor = new EditorPanel();
		tabbedPane.addTab("Editor", null, tabPanel_Editor, null);
		
		// Export panel
		JPanel tabPanel_Export = new ExportPanel();
		tabbedPane.addTab("Export", null, tabPanel_Export, null);
		
	}
	
}