package nl.andrewlalis.blockbookbinder;

import nl.andrewlalis.blockbookbinder.view.MainFrame;

import javax.swing.*;

/**
 * The main class for the application.
 */
public class BlockBookBinder {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			var mainFrame = new MainFrame();
			mainFrame.setupAndShow();
		});
	}
}
