package nl.andrewlalis.blockbookbinder.control;

import nl.andrewlalis.blockbookbinder.view.SourceTextPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener for an action where the source text is 'cleaned' by removing any
 * trailing whitespace, and removing unnecessary newlines.
 */
public class CleanSourceActionListener implements ActionListener {
	private final SourceTextPanel sourceTextPanel;

	public CleanSourceActionListener(SourceTextPanel sourceTextPanel) {
		this.sourceTextPanel = sourceTextPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String source = this.sourceTextPanel.getSourceText();
		String updated = source.trim()
				.replaceAll("(?>\\v)+(\\v)", "\n\n") // Replace large chunks of newline with just two.
				.replace("  ", " "); // Remove any double spaces.
		updated = this.removeNewlineWrapping(updated);
		this.sourceTextPanel.setSourceText(updated);
	}

	private String removeNewlineWrapping(String source) {
		final StringBuilder sb = new StringBuilder(source.length());
		final char[] sourceChars = source.toCharArray();
		for (int i = 0; i < sourceChars.length; i++) {
			char c = sourceChars[i];
			if (
					c == '\n'
							&& (i - 1 >= 0 && !Character.isWhitespace(sourceChars[i - 1]))
							&& (i + 1 < sourceChars.length && !Character.isWhitespace(sourceChars[i + 1]))
			) {
				c = ' ';
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
