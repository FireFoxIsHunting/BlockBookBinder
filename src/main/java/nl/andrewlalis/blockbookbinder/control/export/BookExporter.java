package nl.andrewlalis.blockbookbinder.control.export;

import lombok.Setter;
import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A separate runnable process which handles exporting a book, page by page,
 * into one's clipboard and pasting the pages.
 */
public class BookExporter implements Runnable {
	private final static int START_DELAY = 10;
	private final static int CLIPBOARD_RETRY_DELAY_MS = 100;
	private final static int ROBOT_ACTION_DELAY_MS = 100;

	private final Book book;
	private final boolean autoPaste;

	@Setter
	private volatile boolean running;

	@Setter
	private volatile boolean nextPageRequested;

	private final PagePasteListener pagePasteListener;
	private final Clipboard clipboard;
	private Robot robot;

	// Some sound clips to play as user feedback.
	private final Clip beepClip;
	private final Clip beginningExportClip;

	public BookExporter(Book book, boolean autoPaste) {
		this.book = book;
		this.autoPaste = autoPaste;
		this.beepClip = this.loadAudioClip(ApplicationProperties.getProp("export_dialog.beep_sound"));
		this.beginningExportClip = this.loadAudioClip(ApplicationProperties.getProp("export_dialog.beginning_export"));
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		this.pagePasteListener = new PagePasteListener(this);
		if (this.autoPaste) { // Only initialize the robot if we'll need it.
			try {
				this.robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		this.running = true;
		this.nextPageRequested = true;
		long startTime = System.currentTimeMillis();
		long lastAudioPlayedAt = 0;
		long lastPageExportedAt = 0;
		int nextPageToExport = 0;
		while (this.running) {
			long currentTime = System.currentTimeMillis();
			// Check if we're still in the first few seconds of runtime.
			boolean inStartPhase = (currentTime - startTime < (START_DELAY * 1000));
			if (inStartPhase && currentTime - lastAudioPlayedAt > 1000) {
				this.playAudioClip(this.beepClip);
				lastAudioPlayedAt = currentTime;
			}
			// Otherwise, export one page.
			if (!inStartPhase && this.nextPageRequested) {
				System.out.println("Page requested: " + nextPageToExport);
				this.nextPageRequested = false; // Reset the flag so that some other process has to set it before the next page is exported.
				// If this is the first time we're exporting, play a sound.
				if (lastPageExportedAt == 0) {
					this.initNativeListener();
					this.playAudioClip(this.beginningExportClip);
				}
				this.exportPageToClipboard(nextPageToExport);
				if (this.autoPaste) {
					this.pasteAndTurnPage();
				}
				this.playAudioClip(this.beepClip);
				nextPageToExport++;
				// If we've reached the end of the book, stop the exporter.
				if (nextPageToExport >= this.book.getPageCount()) {
					System.out.println("Export finished: " + this.book.getPageCount() + " pages exported.");
					if (!this.autoPaste) {
						this.stopNativeListener();
					}
					this.running = false;
					break;
				}
				// Since there may be significant delay, get a fresh timestamp.
				lastPageExportedAt = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Loads the given page onto the system clipboard so either a user or this
	 * program can paste it into a minecraft book.
	 * @param page The index of the page to export.
	 */
	private void exportPageToClipboard(int page) {
		boolean clipboardSuccess = false;
		int attempts = 0;
		while (!clipboardSuccess) {
			try {
				attempts++;
				clipboard.setContents(new StringSelection(book.getPages().get(page).toString()), null);
				clipboardSuccess = true;
			} catch (IllegalStateException e) {
				System.err.println("Could not open and set contents of system clipboard.");
			}
			if (!clipboardSuccess) {
				try {
					Thread.sleep(CLIPBOARD_RETRY_DELAY_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (attempts > 10) {
				throw new RuntimeException("Could not insert page into clipboard after " + attempts + " attempts.");
			}
		}
		System.out.println("Exported page " + page + " to clipboard.");
	}

	/**
	 * Automatically pastes and turns the page of the minecraft book, so that
	 * the next page can be pasted in.
	 */
	private void pasteAndTurnPage() {
		this.robot.keyPress(KeyEvent.VK_CONTROL);
		this.robot.keyPress(KeyEvent.VK_V);
		try {
			Thread.sleep(ROBOT_ACTION_DELAY_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.robot.keyRelease(KeyEvent.VK_V);
		this.robot.keyRelease(KeyEvent.VK_CONTROL);
		System.out.println("Pasted.");
		this.robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
		try {
			Thread.sleep(ROBOT_ACTION_DELAY_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
		try { // Wait for minecraft to turn the page.
			Thread.sleep(ROBOT_ACTION_DELAY_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Clicked mouse.");
		this.nextPageRequested = true;
	}

	private void initNativeListener() {
		try {
			// For catching native events, set logging here.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.WARNING);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(this.pagePasteListener);
		} catch (NativeHookException nativeHookException) {
			System.err.println("Could not register native hook.");
			nativeHookException.printStackTrace();
		}
	}

	private void stopNativeListener() {
		try {
			GlobalScreen.removeNativeKeyListener(this.pagePasteListener);
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException nativeHookException) {
			System.err.println("Could not unregister a native hook.");
			nativeHookException.printStackTrace();
		}
	}

	private void playAudioClip(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}

	private Clip loadAudioClip(String path) {
		try {
			Clip clip = AudioSystem.getClip();
			InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream(path);
			if (fileInputStream == null) {
				return null;
			}
			AudioInputStream ais = AudioSystem.getAudioInputStream(fileInputStream);
			clip.open(ais);
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			System.err.println("Could not load audio clip.");
			e.printStackTrace();
			return null;
		}
	}
}
