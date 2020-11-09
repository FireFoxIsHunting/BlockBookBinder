package nl.andrewlalis.blockbookbinder.control.export;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class PagePasteListener implements NativeKeyListener {
	private BookExporter exporterRunnable;

	public PagePasteListener(BookExporter exporterRunnable) {
		this.exporterRunnable = exporterRunnable;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_V && (nativeKeyEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0) {
			// Wait a little bit so that we can let the system do whatever it was planning to do with the original paste action.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.exporterRunnable.setNextPageRequested(true);
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}
}
