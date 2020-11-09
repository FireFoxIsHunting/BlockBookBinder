package nl.andrewlalis.blockbookbinder.view.about;

import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

/**
 * Simple dialog whose main purpose is rendering an HTML document.
 */
public class AboutDialog extends JDialog {
	public AboutDialog(Frame owner) {
		super(owner, "About", true);
	}

	public void setupAndShow() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(
				ApplicationProperties.getIntProp("about_dialog.min_width"),
				ApplicationProperties.getIntProp("about_dialog.min_height")
		));
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		textPane.setText(this.getAboutHtml());
		textPane.addHyperlinkListener(e -> {
			try {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					Desktop.getDesktop().browse(e.getURL().toURI());
				}
			} catch (URISyntaxException | IOException uriSyntaxException) {
				uriSyntaxException.printStackTrace();
			}
		});
		JScrollPane scrollPane = new JScrollPane(
				textPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		this.setContentPane(scrollPane);
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
	}

	private String getAboutHtml() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				ApplicationProperties.getProp("about_dialog.source")
		);
		if (is == null) {
			return "";
		}
		return new BufferedReader(new InputStreamReader(is)).lines()
				.parallel().collect(Collectors.joining("\n"));
	}
}
