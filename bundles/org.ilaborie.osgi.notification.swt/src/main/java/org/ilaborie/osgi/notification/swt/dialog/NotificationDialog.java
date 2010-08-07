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

/**
 * The Class NotificationDialog.
 *
 * @author igor
 */
public class NotificationDialog {

	/** The default transparency. */
	private static final int TRANSPARENCY_DEFAULT = 0xEE;

	// Factory
	/**
	 * Creates the notification dialog.
	 *
	 * @param colors the colors
	 * @param notification the notification
	 * @param listener the listener
	 * @return the notification dialog
	 */
	public static NotificationDialog createNotificationDialog(
			INotificationColors colors, INotification notification,
			Listener listener) {
		Display display = Display.getDefault();
		NotificationDialog result = new NotificationDialog(display, colors,
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

	/** The colors. */
	private final INotificationColors colors;

	// Constructor
	/**
	 * Instantiates a new notification dialog.
	 *
	 * @param display the display
	 * @param colors the colors
	 * @param notification the notification
	 * @param listener the listener
	 */
	private NotificationDialog(Display display, INotificationColors colors,
			INotification notification, Listener listener) {
		super();
		assert display != null;
		this.display = display;
		this.colors = colors;
		this.notification = notification;
		this.listener = listener;
	}

	// Methods

	/**
	 * Show.
	 * No fading
	 */
	public void show() {
		this.show(false);
	}

	/**
	 * Show.
	 *
	 * @param fade the fading option
	 */
	public void show(boolean fade) {
		this.shell = new Shell(this.display, SWT.NO_TRIM | SWT.ON_TOP);
		this.configureShell();
		this.shell.pack();

		// TODO Position

		// TODO Region

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
	 */
	protected void configureShell() {
		assert this.display != null;
		assert this.shell != null;
		assert this.notification != null;

		// TODO Check Layout 
		// TODO fonts

		// Shell
		this.shell.setLayout(new GridLayout());
		this.shell.setAlpha(TRANSPARENCY_DEFAULT);
		if (this.colors != null) {
			this.shell.setBackground(this.colors.getBackgroundColor());
			this.shell.setForeground(this.colors.getForegroundColor());
		}

		// Icon
		this.lblIcon = new Label(this.shell, SWT.NONE);
		// TODO set image from url
		if (this.colors != null) {
			this.lblIcon.setBackground(this.colors.getBackgroundColor());
		}
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
		gridData.verticalSpan = 2;
		this.lblIcon.setLayoutData(gridData);

		// Title
		this.lblTitle = new Label(this.shell, SWT.NONE);
		if (this.notification.getTitle() != null) {
			this.lblTitle.setText(this.notification.getTitle());
		}
		if (this.colors != null) {
			this.lblTitle.setForeground(this.colors.getTitleColor());
			this.lblTitle.setBackground(this.colors.getBackgroundColor());
		}
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.lblTitle.setLayoutData(gridData);

		// Message
		this.lblMessage = new Label(this.shell, SWT.NONE);
		this.lblMessage.setText(this.notification.getMessage());
		if (this.colors != null) {
			this.lblMessage.setForeground(this.colors.getMessageColor());
			this.lblMessage.setBackground(this.colors.getBackgroundColor());
		}
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.minimumWidth = 200;
		this.lblMessage.setLayoutData(gridData);
	}
}
