package nl.andrewlalis.blockbookbinder.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BookContents {
	@Getter
	private final List<BookPage> pages;

	public BookContents() {
		this.pages = new ArrayList<>();
	}
}
