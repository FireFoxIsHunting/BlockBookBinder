package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.view.BookPreviewPanel;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;

/**
 * Listener that listens for native key-presses that indicate the user has
 * pasted something into a book.
 */
public class BookPagePasteListener implements NativeKeyListener {
	private final Book book;
	private final Clipboard clipboard;
	private final BookPreviewPanel bookPreviewPanel;
	private final JButton cancelExportButton;
	private final ActionListener cancelExportActionListener;
	private int nextPage;

	public BookPagePasteListener(Book book, Clipboard clipboard, BookPreviewPanel bookPreviewPanel, JButton cancelExportButton) {
		this.book = book;
		this.clipboard = clipboard;
		this.bookPreviewPanel = bookPreviewPanel;
		this.cancelExportButton = cancelExportButton;
		this.nextPage = 0;
		this.cancelExportActionListener = (e) -> this.cancelExport();
		this.cancelExportButton.addActionListener(this.cancelExportActionListener);
	}

	public void exportNextPage() {
		// Sleep a little bit to avoid rapid repeats.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.bookPreviewPanel.setCurrentPage(this.nextPage);
		boolean clipboardSuccess = false;
		while (!clipboardSuccess) {
			try {
				clipboard.setContents(
						new StringSelection(book.getPages().get(this.nextPage).toString()),
						null
				);
				clipboardSuccess = true;
			} catch (IllegalStateException e) {
				System.err.println("Could not open and set contents of system clipboard.");
			}
			if (!clipboardSuccess) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Copied page " + this.nextPage + " into clipboard.");
		this.nextPage++;

		// If we've reached the end of the book, unregister this listener and remove native hooks.
		if (this.nextPage >= this.book.getPageCount()) {
			this.cancelExport();
		}
	}

	public void cancelExport() {
		try {
			this.bookPreviewPanel.enableNavigation(true);
			this.cancelExportButton.setEnabled(false);
			this.cancelExportButton.removeActionListener(this.cancelExportActionListener);
			GlobalScreen.removeNativeKeyListener(this);
			GlobalScreen.unregisterNativeHook();
			System.out.println("Done pasting.");
		} catch (NativeHookException nativeHookException) {
			System.err.println("Could not unregister a native hook.");
			nativeHookException.printStackTrace();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_V && (nativeKeyEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0) {
			this.exportNextPage();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
}
