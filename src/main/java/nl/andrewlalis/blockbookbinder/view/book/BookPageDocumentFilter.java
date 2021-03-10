package nl.andrewlalis.blockbookbinder.view.book;

import lombok.Setter;
import nl.andrewlalis.blockbookbinder.model.BookPage;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class BookPageDocumentFilter extends DocumentFilter {
	@Setter
	private BookPage page;

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		System.out.printf("Remove: offset=%d, length=%d\n", offset, length);
		if (page == null) return;
		super.remove(fb, offset, length);
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		System.out.printf("Insert: offset=%d, string=%s\n", offset, string);
		if (page == null) return;
		int lineIndex = page.getLineIndexAtOffset(offset);
		System.out.printf("Insert on line %d: %s\n", lineIndex, page.getLine(lineIndex));
		fb.getDocument().getStartPosition().getOffset();
		super.insertString(fb, offset, string, attr);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		System.out.printf("Replace: offset=%d, length=%d, text=%s\n", offset, length, text);
		if (page == null) return;
		int lineIndex = page.getLineIndexAtOffset(offset);
		System.out.printf("Replace on line %d: %s\n", lineIndex, page.getLine(lineIndex));
		super.replace(fb, offset, length, text, attrs);
	}
}
