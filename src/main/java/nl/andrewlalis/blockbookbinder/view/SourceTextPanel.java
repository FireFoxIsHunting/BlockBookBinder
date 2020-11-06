package nl.andrewlalis.blockbookbinder.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A panel dedicated to displaying an interacting with a raw source of text for
 * a book.
 */
public class SourceTextPanel extends JPanel {
	public SourceTextPanel() {
		super(new BorderLayout());

		this.add(new JLabel("Source Text"), BorderLayout.NORTH);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

		JTextArea mainTextArea = new JTextArea();
		JScrollPane scrollWrappedMainTextArea = new JScrollPane(mainTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollWrappedMainTextArea, BorderLayout.CENTER);

		JPanel rightPanelButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.add(rightPanelButtonPanel, BorderLayout.SOUTH);
		JButton importButton = new JButton("Import");
		importButton.setActionCommand("importSource");
		rightPanelButtonPanel.add(importButton);
	}
}
