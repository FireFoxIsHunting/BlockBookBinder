package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.view.BookPreviewPanel;
import nl.andrewlalis.blockbookbinder.view.export.ExportToBookDialog;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Action listener that is used for when a user decides to begin exporting a
 * book to minecraft.
 */
public class BookExportActionListener implements ActionListener {
	private final BookPreviewPanel bookPreviewPanel;
	private final Clipboard clipboard;
	private final JButton cancelExportButton;

	public BookExportActionListener(BookPreviewPanel bookPreviewPanel, JButton cancelExportButton) {
		this.bookPreviewPanel = bookPreviewPanel;
		this.cancelExportButton = cancelExportButton;
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Starting export.");
		final Book book = this.bookPreviewPanel.getBook();
		int choice = JOptionPane.showConfirmDialog(
				this.bookPreviewPanel.getRootPane(),
				"Press OK to initialize export.",
				"Confirm Export",
				JOptionPane.OK_CANCEL_OPTION
		);
		if (choice == JOptionPane.CANCEL_OPTION) {
			return;
		}
		this.cancelExportButton.setEnabled(true);
		BookPagePasteListener pasteListener = new BookPagePasteListener(book, clipboard, this.bookPreviewPanel, this.cancelExportButton);
		this.bookPreviewPanel.enableNavigation(false);
		pasteListener.exportNextPage(); // Start by exporting the first page right away.
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
