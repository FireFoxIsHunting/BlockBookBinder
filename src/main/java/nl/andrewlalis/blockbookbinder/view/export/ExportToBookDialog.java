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
	private JSpinner autoPasteDelaySpinner;

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
		this.setLocationRelativeTo(this.getOwner());
		this.setVisible(true);
	}

	private Container buildContentPane() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel setupPanel = new JPanel();
		setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.PAGE_AXIS));
		this.autoCheckbox = new JCheckBox("Auto-paste", true);
		this.firstPageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, this.book.getPageCount(), 1));
		JPanel firstPageSpinnerPanel = new JPanel(new BorderLayout());
		firstPageSpinnerPanel.add(new JLabel("First Page:"), BorderLayout.WEST);
		firstPageSpinnerPanel.add(this.firstPageSpinner, BorderLayout.CENTER);
		this.lastPageSpinner = new JSpinner(new SpinnerNumberModel(this.book.getPageCount(), 1, this.book.getPageCount(), 1));
		JPanel lastPageSpinnerPanel = new JPanel(new BorderLayout());
		lastPageSpinnerPanel.add(new JLabel("Last Page:"), BorderLayout.WEST);
		lastPageSpinnerPanel.add(this.lastPageSpinner, BorderLayout.CENTER);
		this.autoPasteDelaySpinner = new JSpinner(new SpinnerNumberModel(0.2, 0.1, 5.0, 0.1));
		JPanel autoPasteDelaySpinnerPanel = new JPanel(new BorderLayout());
		autoPasteDelaySpinnerPanel.add(new JLabel("Auto-Paste Delay (s):"), BorderLayout.WEST);
		autoPasteDelaySpinnerPanel.add(this.autoPasteDelaySpinner, BorderLayout.CENTER);
		setupPanel.add(this.autoCheckbox);
		setupPanel.add(firstPageSpinnerPanel);
		setupPanel.add(lastPageSpinnerPanel);
		setupPanel.add(autoPasteDelaySpinnerPanel);

		this.exportStatusPanel = new ExportStatusPanel();

		this.centerCardPanel = new JPanel(new CardLayout());
		centerCardPanel.add(setupPanel, SETUP_CARD);
		centerCardPanel.add(this.exportStatusPanel, STATUS_CARD);
		this.showCardByName(SETUP_CARD);

		mainPanel.add(centerCardPanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.startButton = new JButton("Start");
		this.startButton.addActionListener(e -> {
			if (!this.checkSpinnerValues()){
				return;
			}
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

	/**
	 * Starts up the exporter thread.
	 */
	private void startExporter() {
		final int firstPage = (int) this.firstPageSpinner.getValue();
		final int lastPage = (int) this.lastPageSpinner.getValue();
		final Book pagesRange = this.book.getPageRange(firstPage - 1, lastPage - firstPage + 1);

		final double autoPasteDelay = (double) this.autoPasteDelaySpinner.getValue();
		final int autoPasteDelayMillis = (int) (autoPasteDelay * 1000);

		this.startButton.setEnabled(false);
		this.startButton.setVisible(false);
		this.stopButton.setEnabled(true);
		this.stopButton.setVisible(true);
		this.showCardByName(STATUS_CARD);
		this.exporterRunnable = new BookExporter(
				this,
				this.exportStatusPanel,
				pagesRange,
				this.autoCheckbox.isSelected(),
				autoPasteDelayMillis
		);
		this.exporterThread = new Thread(this.exporterRunnable);
		this.exporterThread.start();
	}

	/**
	 * Shuts down the exporter thread.
	 */
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

	/**
	 * This method is called by the exporter thread once it is done.
	 */
	public void onExportFinished() {
		JOptionPane.showMessageDialog(
				this,
				"Book export has finished.",
				"Export Complete",
				JOptionPane.INFORMATION_MESSAGE
		);
		this.stopExporter();
		this.dispose();
	}

	private void showCardByName(String name) {
		CardLayout cl = (CardLayout) this.centerCardPanel.getLayout();
		cl.show(this.centerCardPanel, name);
	}

	/**
	 * Checks the values of the spinners that are used to select the first and
	 * last pages, and shows a popup warning if they're not correct.
	 */
	private boolean checkSpinnerValues() {
		final int firstPage = (int) this.firstPageSpinner.getValue();
		final int lastPage = (int) this.lastPageSpinner.getValue();

		if (
				firstPage < 1
				|| lastPage > this.book.getPageCount()
				|| firstPage > lastPage
				|| (lastPage - firstPage + 1 > ApplicationProperties.getIntProp("book.max_pages"))
		) {
			JOptionPane.showMessageDialog(
					this,
					"Invalid page range. Please follow the rules below:\n" +
							"1. First page must be lower or equal to the last page.\n" +
							"2. Number of pages to export cannot exceed 100.\n",
					"Invalid Page Range",
					JOptionPane.WARNING_MESSAGE
			);
			return false;
		}
		return true;
	}
}
