package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.model.Book;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class BookPagePasteListener implements NativeKeyListener {
	private final Book book;
	private final Clipboard clipboard;
	private int nextPage;

	public BookPagePasteListener(Book book, Clipboard clipboard) {
		this.book = book;
		this.clipboard = clipboard;
		this.nextPage = 0;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_V && (nativeKeyEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0) {
			System.out.println("CTRL + V -> Paste!!!");
			clipboard.setContents(
					new StringSelection(book.getPages().get(this.nextPage).toString()),
					null
			);
			this.nextPage++;
			System.out.println("Incremented page.");
			if (this.nextPage >= this.book.getPageCount()) {
				try {
					GlobalScreen.removeNativeKeyListener(this);
					GlobalScreen.unregisterNativeHook();
					System.out.println("Done pasting.");
				} catch (NativeHookException nativeHookException) {
					System.err.println("Could not unregister a native hook.");
					nativeHookException.printStackTrace();
				}
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
}
