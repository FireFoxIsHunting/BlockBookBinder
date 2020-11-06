package nl.andrewlalis.blockbookbinder.control;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ImportAction extends AbstractAction {
	public ImportAction(String name) {
		super(name);
	}

	public ImportAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Import!");
	}
}
