package nl.andrewlalis.blockbookbinder.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class IconLoader {

	public static Icon load(String resourceName, int width, int height) {
		try {
			InputStream is = IconLoader.class.getClassLoader().getResourceAsStream(resourceName);
			if (is == null) {
				throw new IOException("Could not open resource: " + resourceName);
			}
			Image img = ImageIO.read(is).getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(img);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
