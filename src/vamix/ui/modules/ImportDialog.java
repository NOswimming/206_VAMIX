package vamix.ui.modules;

import java.awt.BorderLayout;

import javax.swing.JDialog;

public class ImportDialog extends JDialog{
	
	public ImportDialog() {
		setTitle("Import File");
		setContentPane(new ImportPanel()); 
		pack();
	}

}
