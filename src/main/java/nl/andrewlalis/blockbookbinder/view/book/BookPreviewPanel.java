package nl.andrewlalis.blockbookbinder.view.book;

import lombok.Getter;
import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.model.BookPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * A customized panel that's dedicated to showing a book's contents.
 */
public class BookPreviewPanel extends JPanel {
	@Getter
	private Book book;
	private int currentPage = 0;

	private final JEditorPane pageEditorPane;
	private final BookPageDocumentFilter documentFilter;
	private final JLabel titleLabel;

	private final JButton previousPageButton;
	private final JButton nextPageButton;
	private final JButton firstPageButton;
	private final JButton lastPageButton;

	public BookPreviewPanel() {
		super(new BorderLayout());

		this.titleLabel = new JLabel("Book Preview");
		this.add(this.titleLabel, BorderLayout.NORTH);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.pageEditorPane = new JEditorPane();
		this.documentFilter = new BookPageDocumentFilter();
		AbstractDocument doc = (AbstractDocument) this.pageEditorPane.getDocument();
		doc.setDocumentFilter(this.documentFilter);
		this.pageEditorPane.setEditable(true);
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("fonts/1_Minecraft-Regular.otf");
			if (is == null) {
				throw new IOException("Could not read minecraft font.");
			}
			Font mcFont = Font.createFont(Font.TRUETYPE_FONT, is);
			mcFont = mcFont.deriveFont(24.0f);
			this.pageEditorPane.setFont(mcFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		JScrollPane previewPageScrollPane = new JScrollPane(this.pageEditorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(previewPageScrollPane, BorderLayout.CENTER);

		JPanel previewButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.firstPageButton = new JButton("First");
		this.firstPageButton.addActionListener(e -> {
			this.currentPage = 0;
			displayCurrentPage();
		});

		this.previousPageButton = new JButton("Previous Page");
		this.previousPageButton.addActionListener(e -> {
			if (currentPage > 0) {
				currentPage--;
				displayCurrentPage();
			}
		});

		this.nextPageButton = new JButton("Next Page");
		this.nextPageButton.addActionListener(e -> {
			if (currentPage < book.getPageCount() - 1) {
				currentPage++;
				displayCurrentPage();
			}
		});

		this.lastPageButton = new JButton("Last");
		this.lastPageButton.addActionListener(e -> {
			this.currentPage = Math.max(this.book.getPageCount() - 1, 0);
			displayCurrentPage();
		});

		previewButtonPanel.add(this.firstPageButton);
		previewButtonPanel.add(this.previousPageButton);
		previewButtonPanel.add(this.nextPageButton);
		previewButtonPanel.add(this.lastPageButton);
		this.add(previewButtonPanel, BorderLayout.SOUTH);
		Book starterBook = new Book();
		starterBook.addPage(new BookPage());
		this.setBook(starterBook);
	}

	private void displayCurrentPage() {
		if (this.book.getPageCount() == 0) {
			return;
		}
		BookPage currentPage = this.book.getPages().get(this.currentPage);
		this.pageEditorPane.setText(currentPage.toString());
		this.titleLabel.setText("Book Preview (Page " + (this.currentPage + 1) + " of " + this.book.getPageCount() + ")");
	}

	public void setBook(Book book) {
		this.book = book;
		this.setCurrentPage(0);
	}

	public void setCurrentPage(int page) {
		this.currentPage = page;
		this.documentFilter.setPage(this.book.getPage(page));
		this.displayCurrentPage();
	}

	public void enableNavigation(boolean enabled) {
		this.firstPageButton.setEnabled(enabled);
		this.previousPageButton.setEnabled(enabled);
		this.nextPageButton.setEnabled(enabled);
		this.lastPageButton.setEnabled(enabled);
	}
}
