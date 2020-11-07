package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.view.BookPreviewPanel;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookExportActionListener implements ActionListener {
	private final BookPreviewPanel bookPreviewPanel;
	private final Clipboard clipboard;

	public BookExportActionListener(BookPreviewPanel bookPreviewPanel) {
		this.bookPreviewPanel = bookPreviewPanel;
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Starting export.");
		final Book book = this.bookPreviewPanel.getBook();
		BookPagePasteListener pasteListener = new BookPagePasteListener(book, clipboard);
		try {
			// For catching native events, set logging here.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(pasteListener);
		} catch (NativeHookException nativeHookException) {
			System.err.println("Could not register native hook.");
			nativeHookException.printStackTrace();
		}
	}
}
