package nl.andrewlalis.blockbookbinder.model;

import lombok.Getter;
import lombok.Setter;

public class BookPage {
	@Getter
	@Setter
	private String content;

	public BookPage() {
		this.content = "";
	}
}
