package nl.andrewlalis.blockbookbinder;

import nl.andrewlalis.blockbookbinder.view.MainFrame;
import org.jnativehook.GlobalScreen;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
