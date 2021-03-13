package nl.andrewlalis.blockbookbinder.model;

import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

import java.util.Arrays;

public class BookPage {
	public static final int MAX_LINES = ApplicationProperties.getIntProp("book.page_max_lines");
	private final String[] lines;

	public BookPage() {
		this.lines = new String[MAX_LINES];
		Arrays.fill(this.lines, "");
	}

	private BookPage(String[] lines) {
		this.lines = lines;
	}

	public void setLine(int index, String line) {
		if (index < 0 || index >= this.lines.length) {
			throw new IndexOutOfBoundsException(index);
		}
		this.lines[index] = line;
	}

	public String getLine(int index) {
		if (index < 0 || index >= this.lines.length) {
			throw new IndexOutOfBoundsException(index);
		}
		return this.lines[index];
	}

	/**
	 * Gets the index of the line at which this offset occurs.
	 * @param offset The offset, from the start of the page.
	 * @return The index of the line in which the given offset is placed.
	 */
	public int getLineIndexAtOffset(int offset) {
		int lineIndex = 0;
		String line = this.getLine(lineIndex);
		while (offset - line.length() > 0) {
			offset -= line.length();
			line = this.getLine(lineIndex++);
		}
		return lineIndex;
	}

	public boolean hasContent() {
		for (String line : this.lines) {
			if (!line.isBlank()) {
				return true;
			}
		}
		return false;
	}

	public BookPage copy() {
		return new BookPage(Arrays.copyOf(this.lines, MAX_LINES));
	}

	@Override
	public String toString() {
		return String.join("\n", this.lines);
	}
}
