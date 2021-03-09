package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.model.build.BookBuilder;
import nl.andrewlalis.blockbookbinder.view.book.BookPreviewPanel;
import nl.andrewlalis.blockbookbinder.view.SourceTextPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener that, when activated, converts the text from the source panel
 * into a formatted book.
 */
public class ConvertToBookActionListener implements ActionListener {
	private final SourceTextPanel sourceTextPanel;
	private final BookPreviewPanel bookPreviewPanel;

	public ConvertToBookActionListener(SourceTextPanel sourceTextPanel, BookPreviewPanel bookPreviewPanel) {
		this.sourceTextPanel = sourceTextPanel;
		this.bookPreviewPanel = bookPreviewPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.bookPreviewPanel.setBook(
				new BookBuilder().build(this.sourceTextPanel.getSourceText())
		);
	}
}
