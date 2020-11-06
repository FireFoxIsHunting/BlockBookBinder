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
}
