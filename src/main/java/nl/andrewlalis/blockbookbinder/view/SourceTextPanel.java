package nl.andrewlalis.blockbookbinder.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A panel dedicated to displaying an interacting with a raw source of text for
 * a book.
 */
public class SourceTextPanel extends JPanel {
	private final JTextArea textArea;

	public SourceTextPanel() {
		super(new BorderLayout());

		this.add(new JLabel("Source Text"), BorderLayout.NORTH);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.textArea = new JTextArea();
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		JScrollPane scrollWrappedMainTextArea = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollWrappedMainTextArea, BorderLayout.CENTER);
	}

	public String getSourceText() {
		return this.textArea.getText();
	}

	public void setSourceText(String text) {
		this.textArea.setText(text);
	}
}
