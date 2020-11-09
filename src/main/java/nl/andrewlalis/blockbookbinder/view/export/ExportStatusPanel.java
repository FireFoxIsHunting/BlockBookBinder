package nl.andrewlalis.blockbookbinder.view.export;

import javax.swing.*;
import java.awt.*;

/**
 * A panel with some components for displaying the current status of an export
 * job.
 */
public class ExportStatusPanel extends JPanel {
	private JProgressBar exportProgressBar;

	public ExportStatusPanel() {
		this.setLayout(new BorderLayout());

		this.exportProgressBar = new JProgressBar();
	}
}
