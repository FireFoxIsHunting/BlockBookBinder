package nl.andrewlalis.blockbookbinder;

import com.formdev.flatlaf.FlatDarkLaf;
import nl.andrewlalis.blockbookbinder.view.MainFrame;

import javax.swing.*;

/**
 * The main class for the application.
 */
public class BlockBookBinder {
	public static void main(String[] args) {
		FlatDarkLaf.install();
		SwingUtilities.invokeLater(() -> {
			var mainFrame = new MainFrame();
			mainFrame.setupAndShow();
		});
	}
}
