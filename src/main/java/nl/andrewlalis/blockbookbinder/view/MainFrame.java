package nl.andrewlalis.blockbookbinder.view;

import nl.andrewlalis.blockbookbinder.control.ImportAction;
import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

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
		doublePanel.add(new BookPreviewPanel());
		doublePanel.add(new SourceTextPanel());
		mainPanel.add(doublePanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton exportButton = new JButton("Export to Book");
		exportButton.addActionListener(e -> {
			System.out.println("Starting export.");
//			final String fullText = mainTextArea.getText().trim();
//			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fullText.substring(0, 200)), null);
		});
		bottomPanel.add(exportButton);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		return mainPanel;
	}
}
