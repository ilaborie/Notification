/**
 * 
 */
package org.ilaborie.osgi.notification.swt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.swt.INotificationColors;
import org.ilaborie.osgi.notification.swt.INotificationFonts;

/**
 * The Class NotificationDialog.
 *
 * @author igor
 */
public class NotificationDialog {

	/** The default transparency. */
	private static final int TRANSPARENCY_DEFAULT = 0xEE;

	/** The vertical padding. */
	private static final int VERTICAL_PADDING = 5;

	/** The horizontal padding. */
	private static final int HORIZONTAL_PADDING = 5;

	// Factory
	/**
	 * Creates the notification dialog.
	 *
	 * @param notification the notification
	 * @param listener the listener
	 * @return the notification dialog
	 */
	public static NotificationDialog createNotificationDialog(
			INotification notification, Listener listener) {
		Display display = Display.getDefault();
		NotificationDialog result = new NotificationDialog(display,
				notification, listener);
		return result;
	}

	// Attributes
	/** The display. */
	private final Display display;

	/** The notification. */
	private final INotification notification;

	/** The shell. */
	private Shell shell;

	/** The label tile. */
	private Label lblTitle;

	/** The label message. */
	private Label lblMessage;

	/** The label icon. */
	private Label lblIcon;

	/** The listener. */
	private final Listener listener;

	// Constructor
	/**
	 * Instantiates a new notification dialog.
	 *
	 * @param display the display
	 * @param notification the notification
	 * @param listener the listener
	 */
	private NotificationDialog(Display display, INotification notification,
			Listener listener) {
		super();
		assert display != null;
		this.display = display;
		this.notification = notification;
		this.listener = listener;
	}

	// Methods

	/**
	 * Show.
	 * No fading
	 *
	 * @param colors the colors
	 * @param fonts the fonts
	 */
	public void show(INotificationColors colors, INotificationFonts fonts) {
		this.show(colors, fonts, false);
	}

	/**
	 * Show.
	 *
	 * @param colors the colors
	 * @param fonts the fonts
	 * @param fade the fading option
	 */
	public void show(INotificationColors colors, INotificationFonts fonts,
			boolean fade) {
		this.shell = new Shell(this.display, SWT.NO_TRIM | SWT.ON_TOP);
		this.configureShell(colors, fonts);
		this.shell.pack();

		// TODO Position

		// TODO Region (round rectangle)

		// Register selection listener
		if (this.listener != null) {
			this.shell.addListener(SWT.Selection, this.listener);
		}

		// TODO fading

		// close expireTime
		this.display.timerExec((int) this.notification.getExpireTime(),
				new ShellCloser(this.shell));

		// Open shell
		this.shell.open(); // TODO use urgency

		while (!this.shell.isDisposed()) {
			if (!this.display.readAndDispatch()) {
				this.display.sleep();
			}
		}
		this.display.dispose();
	}

	/**
	 * Creates the shell content.
	 *
	 * @param colors the colors
	 * @param fonts the fonts
	 */
	protected void configureShell(INotificationColors colors,
			INotificationFonts fonts) {
		assert this.display != null;
		assert this.shell != null;
		assert this.notification != null;

		// TODO fix Layout 

		// Shell
		this.shell.setLayout(new GridLayout());
		this.shell.setAlpha(TRANSPARENCY_DEFAULT);
		if (colors != null) {
			this.shell.setBackground(colors.getBackgroundColor());
			this.shell.setForeground(colors.getForegroundColor());
		}

		// Icon
		this.lblIcon = new Label(this.shell, SWT.NONE);
		// TODO set image from url
		if (colors != null) {
			this.lblIcon.setBackground(colors.getBackgroundColor());
		}
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
		gridData.verticalSpan = 2;
		this.lblIcon.setLayoutData(gridData);

		// Title
		this.lblTitle = new Label(this.shell, SWT.NONE);
		if (this.notification.getTitle() != null) {
			this.lblTitle.setText(this.notification.getTitle());
		}
		if (colors != null) {
			this.lblTitle.setForeground(colors.getTitleColor());
			this.lblTitle.setBackground(colors.getBackgroundColor());
		}
		if (fonts != null) {
			this.lblTitle.setFont(fonts.getTitleFont());
		}
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.lblTitle.setLayoutData(gridData);

		// Message
		this.lblMessage = new Label(this.shell, SWT.NONE);
		this.lblMessage.setText(this.notification.getMessage());
		if (colors != null) {
			this.lblMessage.setForeground(colors.getMessageColor());
			this.lblMessage.setBackground(colors.getBackgroundColor());
		}
		if (fonts != null) {
			this.lblMessage.setFont(fonts.getMessageFont());
		}
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.minimumWidth = 200;
		this.lblMessage.setLayoutData(gridData);
	}
}
