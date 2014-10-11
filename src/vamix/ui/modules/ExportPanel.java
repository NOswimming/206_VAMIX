package vamix.ui.modules;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ExportPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ExportPanel() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_ExportLeft = new JPanel();
		JPanel panel_ExportRight = new JPanel();
		
		JSplitPane splitPane_Export = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_ExportLeft, panel_ExportRight);
		add(splitPane_Export);
		
		splitPane_Export.setLeftComponent(panel_ExportLeft);
		panel_ExportLeft.setLayout(new BorderLayout(0, 0));
		
		Player  exportPlayer = new Player();
		panel_ExportLeft.add(exportPlayer);
		
		splitPane_Export.setRightComponent(panel_ExportRight);
		
		JLabel labelExport = new JLabel("Export");
		panel_ExportRight.add(labelExport);
		

	}

}
