package nl.andrewlalis.blockbookbinder.view.export;

import nl.andrewlalis.blockbookbinder.control.export.BookExporter;
import nl.andrewlalis.blockbookbinder.model.Book;
import nl.andrewlalis.blockbookbinder.util.ApplicationProperties;

import javax.swing.*;
import java.awt.*;

/**
 * A special dialog box that's shown during the process of exporting a book into
 * minecraft.
 */
public class ExportToBookDialog extends JDialog {
	private final static String SETUP_CARD = "SETUP";
	private final static String STATUS_CARD = "STATUS";

	private final Book book;

	// Setup input fields.
	private JCheckBox autoCheckbox;
	private JSpinner firstPageSpinner;
	private JSpinner lastPageSpinner;

	private JButton startButton;
	private JButton stopButton;
	private JPanel centerCardPanel;
	private ExportStatusPanel exportStatusPanel;

	private Thread exporterThread;
	private BookExporter exporterRunnable;

	public ExportToBookDialog(Frame owner, Book book) {
		super(owner, "Export to Book", true);
		this.book = book;
	}

	public void setupAndShow() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setContentPane(this.buildContentPane());
		this.setMinimumSize(new Dimension(
				ApplicationProperties.getIntProp("export_dialog.min_width"),
				ApplicationProperties.getIntProp("export_dialog.min_height")
		));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private Container buildContentPane() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.PAGE_AXIS));
		this.autoCheckbox = new JCheckBox("Auto-paste", true);
		this.firstPageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, this.book.getPageCount(), 1));
		this.lastPageSpinner = new JSpinner(new SpinnerNumberModel(this.book.getPageCount(), 1, this.book.getPageCount(), 1));
		setupPanel.add(this.autoCheckbox);
		setupPanel.add(this.firstPageSpinner);
		setupPanel.add(this.lastPageSpinner);

		this.exportStatusPanel = new ExportStatusPanel();

		this.centerCardPanel = new JPanel(new CardLayout());
		centerCardPanel.add(setupPanel, SETUP_CARD);
		centerCardPanel.add(this.exportStatusPanel, STATUS_CARD);
		this.showCardByName(SETUP_CARD);

		mainPanel.add(centerCardPanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.startButton = new JButton("Start");
		this.startButton.addActionListener(e -> {
			int choice = JOptionPane.showConfirmDialog(
					this.rootPane,
					"Exporting will begin after roughly 10 seconds.\n" +
							"If you have selected \"Auto-paste\", then place\n" +
							"your mouse cursor over the right arrow of the book\n" +
							"so that BlockBookBinder can automatically click it.\n\n" +
							"You have chosen to export pages " + this.firstPageSpinner.getValue() + " to " + this.lastPageSpinner.getValue() + ".",
					"Export Start Confirmation",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE
			);
			if (choice == JOptionPane.OK_OPTION) {
				this.startExporter();
			}
		});
		controlPanel.add(this.startButton);
		this.stopButton = new JButton("Stop");
		this.stopButton.setVisible(false);
		this.stopButton.setEnabled(false);
		this.stopButton.addActionListener(e -> this.stopExporter());
		controlPanel.add(this.stopButton);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);

		return mainPanel;
	}

	private void startExporter() {
		final int firstPage = (int) this.firstPageSpinner.getValue();
		final int lastPage = (int) this.lastPageSpinner.getValue();
		final Book pagesRange = this.book.getPageRange(firstPage - 1, lastPage - firstPage + 1);

		this.startButton.setEnabled(false);
		this.startButton.setVisible(false);
		this.stopButton.setEnabled(true);
		this.stopButton.setVisible(true);
		this.showCardByName(STATUS_CARD);
		this.exporterRunnable = new BookExporter(pagesRange, this.autoCheckbox.isSelected());
		this.exporterThread = new Thread(this.exporterRunnable);
		this.exporterThread.start();
	}

	private void stopExporter() {
		this.exporterRunnable.setRunning(false);
		try {
			this.exporterThread.join();
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
		this.showCardByName(SETUP_CARD);
		this.stopButton.setEnabled(false);
		this.stopButton.setVisible(false);
		this.startButton.setEnabled(true);
		this.startButton.setVisible(true);
	}

	private void showCardByName(String name) {
		CardLayout cl = (CardLayout) this.centerCardPanel.getLayout();
		cl.show(this.centerCardPanel, name);
	}
}
