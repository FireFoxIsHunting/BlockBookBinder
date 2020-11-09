package nl.andrewlalis.blockbookbinder.view;

import nl.andrewlalis.blockbookbinder.control.BookExportActionListener;
import nl.andrewlalis.blockbookbinder.control.ImportAction;
import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;
import nl.andrewlalis.blockbookbinder.view.export.ExportToBookDialog;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * The main window of the application.
 */
public class MainFrame extends JFrame {
	private Action importAction;

	public void setupAndShow() {
		final int width = Integer.parseInt(ApplicationProperties.getProp("frame.default_width"));
		final int height = Integer.parseInt(ApplicationProperties.getProp("frame.default_height"));
		this.setPreferredSize(new Dimension(width, height));
		this.setTitle(ApplicationProperties.getProp("frame.title"));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final URL iconUrl = this.getClass().getClassLoader().getResource("images/book_and_quill.png");
		if (iconUrl != null) {
			this.setIconImage(new ImageIcon(iconUrl).getImage());
		}

		this.initActions();
		this.setContentPane(this.buildContentPane());
		this.setJMenuBar(this.buildMenuBar());

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initActions() {
		this.importAction = new ImportAction("Import");
	}

	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(this.importAction));
		menuBar.add(fileMenu);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		menuBar.add(helpMenu);

		return menuBar;
	}

	private Container buildContentPane() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel doublePanel = new JPanel(new GridLayout(1, 2));
		BookPreviewPanel bookPreviewPanel = new BookPreviewPanel();
		doublePanel.add(bookPreviewPanel);
		SourceTextPanel sourceTextPanel = new SourceTextPanel(bookPreviewPanel);
		doublePanel.add(sourceTextPanel);
		mainPanel.add(doublePanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton exportButton = new JButton("Export to Book");
		JButton cancelExportButton = new JButton("Cancel Export");
		cancelExportButton.setEnabled(false);
		exportButton.addActionListener(e -> {
			final Book book = bookPreviewPanel.getBook();
			if (book == null || book.getPageCount() == 0) {
				JOptionPane.showMessageDialog(this, "Cannot export an empty book.", "Empty Book", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ExportToBookDialog dialog = new ExportToBookDialog(this, bookPreviewPanel.getBook());
			dialog.setupAndShow();
		});
		bottomPanel.add(exportButton);
		bottomPanel.add(cancelExportButton);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		return mainPanel;
	}
}
