package nl.andrewlalis.blockbookbinder.view;

import nl.andrewlalis.blockbookbinder.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A customized panel that's dedicated to showing a book's contents.
 */
public class BookPreviewPanel extends JPanel {
	private Book book;

	public BookPreviewPanel() {
		super(new BorderLayout());

		this.add(new JLabel("Book Preview"), BorderLayout.NORTH);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

		JTextArea previewPageTextArea = new JTextArea();
		JScrollPane previewPageScrollPane = new JScrollPane(previewPageTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(previewPageScrollPane, BorderLayout.CENTER);

		JPanel previewButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		previewButtonPanel.add(new JButton("<"));
		previewButtonPanel.add(new JButton(">"));
		this.add(previewButtonPanel, BorderLayout.SOUTH);
	}
}
