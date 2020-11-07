package nl.andrewlalis.blockbookbinder.model;

import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

/**
 * Class which helps construct formatted book pages from a source text.
 */
public class BookBuilder {
	public Book build(String source) {
		Book book = new Book();
		char[] sourceChars = source.trim().toCharArray();

		final int maxLines = ApplicationProperties.getIntProp("book.page_max_lines");
		final int maxLineWidth = ApplicationProperties.getIntProp("book.page_max_width");
		final CharWidthMapper charWidthMapper = new CharWidthMapper();

		BookPage currentPage = new BookPage();
		StringBuilder lineStringBuilder = new StringBuilder(64);
		int pageLineCount = 1; // Current line on the page we're on.
		int lineCharWidth = 0; // Total pixel width of the current line so far.
		int i = 0;
		while (i < sourceChars.length) {
			final char c = sourceChars[i];
			if (c == '\n') {
				i++;
				continue;
			}
			final int cWidth = charWidthMapper.getWidth(c);
			boolean newLineNeeded = lineCharWidth + cWidth + 1 > maxLineWidth;
			boolean newPageNeeded = pageLineCount == maxLines && newLineNeeded;
			System.out.println("Current char: " + c + ", Current Line: " + pageLineCount + ", Current Line Char Width: " + lineCharWidth + ", New line needed: " + newLineNeeded + ", New page needed: " + newPageNeeded);

			// Check if the page is full, and append it to the book, and refresh.
			if (newPageNeeded) {
				// If necessary, append whatever is left in the last line to the page.
				if (lineStringBuilder.length() > 0) {
					currentPage.addLine(lineStringBuilder.toString());
				}
				book.getPages().add(currentPage);
				currentPage = new BookPage();
				// Reset all buffers and counters for the next page.
				lineStringBuilder.setLength(0);
				newLineNeeded = false;
				pageLineCount = 1;
				lineCharWidth = 0;
			}

			// Check if the line is full, and append it to the page and refresh.
			if (newLineNeeded) {
				currentPage.addLine(lineStringBuilder.toString());
				// Reset line status info.
				lineStringBuilder.setLength(0);
				pageLineCount++;
				lineCharWidth = 0;
			}

			// Finally, append the char to the current line.
			lineStringBuilder.append(c);
			lineCharWidth += cWidth + 1;
			i++;
		}

		// Append a final page with the remainder of the text.
		if (currentPage.hasContent()) {
			if (lineStringBuilder.length() > 0) {
				currentPage.addLine(lineStringBuilder.toString());
			}
			book.getPages().add(currentPage);
		}

		return book;
	}
}
