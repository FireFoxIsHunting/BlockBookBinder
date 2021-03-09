package nl.andrewlalis.blockbookbinder.view.book;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class BookPageDocumentFilter extends DocumentFilter {

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		super.insertString(fb, offset, string, attr);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		super.replace(fb, offset, length, text, attrs);
	}
}
