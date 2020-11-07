package nl.andrewlalis.blockbookbinder.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Book {
	@Getter
	private final List<BookPage> pages;

	public Book() {
		this.pages = new ArrayList<>();
	}

	public int getPageCount() {
		return this.pages.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Book of " + this.getPageCount() + " pages:\n");
		for (int i = 0; i < this.getPageCount(); i++) {
			BookPage page = this.pages.get(i);
			sb.append("Page ").append(i + 1).append(":\n").append(page).append('\n');
		}
		return sb.toString();
	}
}
