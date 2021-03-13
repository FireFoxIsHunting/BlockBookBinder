package nl.andrewlalis.blockbookbinder.control.source;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ImportSourceAction extends AbstractAction {
	@Getter
	private static final ImportSourceAction instance = new ImportSourceAction();

	public ImportSourceAction() {
		super("Import Source");
		this.putValue(SHORT_DESCRIPTION, "Import source text from a file.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
