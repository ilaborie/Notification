/**
 * 
 */
package org.ilaborie.osgi.notification.jface;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.jface.internal.Activator;

/**
 * The Notification Popup.
 *
 * @author igor
 */
public abstract class NotificationPopup extends Window {

	/** The notification. */
	private final INotification notification;

	/** The shell. */
	private Shell shell;

	// Constructors
	/**
	 * Instantiates a new notification popup.
	 *
	 * @param display the display
	 * @param notification the notification
	 */
	public NotificationPopup(Display display, INotification notification) {
		this(display, notification, SWT.NO_TRIM | SWT.ON_TOP | SWT.NO_FOCUS
				| SWT.TOOL);
	}

	/**
	 * Instantiates a new notification popup.
	 *
	 * @param display the display
	 * @param notification the notification
	 * @param style the style
	 */
	public NotificationPopup(Display display, INotification notification,
			int style) {
		super(new Shell(display != null ? display : Display.getCurrent()));
		this.notification = notification;
	}

	// Methods

	/**
	 * Gets the delay.
	 *
	 * @return the delay
	 */
	protected int getDelay() {
		return 5 * 1000; // 5s
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		this.shell = newShell;
		GridLayoutFactory.fillDefaults().applyTo(newShell);
		// TODO set Region
		// TODO set Color
		newShell.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLUE));
		// TODO set Position
		// TODO set Font ...
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		GridLayoutFactory.fillDefaults().applyTo(parent);
		parent.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_YELLOW));

		Composite main = new Composite(parent, SWT.NONE);
		main.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		GridDataFactory.fillDefaults().applyTo(main);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(main);

		// Render Notification Icon
		Label lblMainImage = new Label(main, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).span(1, 2)
				.applyTo(lblMainImage);
		lblMainImage.setImage(this.getNotificationImage(this.notification));

		// Create Header
		Label lblTitle = new Label(main, SWT.WRAP);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(lblTitle);
		this.fillTitleLabel(lblTitle, this.notification);

		// Close button
		Button btnClose = new Button(main, SWT.PUSH);
		btnClose.setImage(Activator.getImage("icons/close.gif")); //$NON-NLS-1$
		btnClose.setToolTipText("Close notification");
		btnClose.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				NotificationPopup.this.setReturnCode(Window.CANCEL);
				NotificationPopup.this.close();
			}
		});

		// Create Message Label
		Label lblMessage = new Label(main, SWT.WRAP);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true)
				.applyTo(lblMessage);
		this.fillMessageLabel(lblMessage, this.notification);

		this.getShell().pack();
		return main;
	}

	/**
	 * Fill message label.
	 *
	 * @param lblMessage the lbl message
	 * @param notification the notification
	 */
	protected void fillMessageLabel(Label lblMessage, INotification notification) {
		lblMessage.setText(this.notification.getMessage());
	}

	/**
	 * Fill title label.
	 *
	 * @param lblTitle the lbl title
	 * @param notification the notification
	 */
	protected void fillTitleLabel(Label lblTitle, INotification notification) {
		if (notification.getTitle() != null) {
			lblTitle.setFont(JFaceResources.getHeaderFont());
			lblTitle.setText(this.notification.getTitle());
		}
	}

	/**
	 * Gets the notification image.
	 *
	 * @param notification the notification
	 * @return the notification image
	 */
	protected abstract Image getNotificationImage(INotification notification);

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#open()
	 */
	@Override
	public int open() {
		if ((this.shell == null) || this.shell.isDisposed()) {
			this.shell = null;
			this.create();
		}

		this.constrainShellSize();
		//		this.shell.setLocation(this.fixupDisplayBounds(this.shell.getSize(),
		//				this.shell.getLocation()));

		this.shell.setVisible(true);

		return this.getReturnCode();
	}
}
