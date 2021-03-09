package nl.andrewlalis.blockbookbinder.model;

import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

import java.util.ArrayList;
import java.util.List;

public class BookPage {
	private final List<String> lines;

	public BookPage() {
		this.lines = new ArrayList<>(ApplicationProperties.getIntProp("book.page_max_lines"));
	}

	public boolean addLine(String line) {
		if (this.lines.size() >= ApplicationProperties.getIntProp("book.page_max_lines")) {
			return false;
		}
		this.lines.add(line);
		return true;
	}

	public boolean hasContent() {
		return !this.lines.isEmpty();
	}

	public BookPage copy() {
		BookPage c = new BookPage();
		for (String line : this.lines) {
			c.addLine(line);
		}
		return c;
	}

	@Override
	public String toString() {
		return String.join("\n", this.lines);
	}

	public static BookPage fromString(String s) {
		BookPage p = new BookPage();
		for (String line : s.split("\n")) {
			p.addLine(line);
		}
		return p;
	}
}
