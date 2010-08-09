/**
 * 
 */
package org.ilaborie.osgi.notification.swt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.swt.INotificationColors;
import org.ilaborie.osgi.notification.swt.INotificationFonts;
import org.ilaborie.osgi.notification.swt.internal.Activator;

/**
 * The Class NotificationDialog.
 *
 * @author igor
 */
public class NotificationDialog {

	// Constants

	/** The radius. */
	private static int RADIUS = 8;

	/** The default transparency. */
	private static final int ALPHA_DEFAULT = 0xEE;

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
		this.show(colors, fonts, true);
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

		// Register selection listener
		if (this.listener != null) {
			this.shell.addListener(SWT.Selection, this.listener);
		}

		// Position
		Point location = this.getLocation();
		this.shell.setLocation(location);

		// Region
		Rectangle bounds = new Rectangle(0, 0, this.shell.getSize().x,
				this.shell.getSize().y);
		Region region = this.buildRoundedRectangularRegion(bounds, RADIUS);
		this.shell.setRegion(region);
		this.shell.setSize(bounds.width + 2 * RADIUS, bounds.height + 2
				* RADIUS);

		// Fading
		if (fade) {
			this.shell.setAlpha(0x00);
			final FadingShell fadingShell = new FadingShell(this.shell,
					ALPHA_DEFAULT, this.notification.getExpireTime());
			// Open shell
			this.shell.open();
			// Fade in
			fadingShell.fadeIn();
			// fadingShell also handle fade out with the timeout 
		} else {
			// No Fading
			this.shell.setAlpha(ALPHA_DEFAULT);
			// Open shell
			this.shell.open();
			// close expireTime
			this.display.timerExec(this.notification.getExpireTime(),
					new ShellCloser(this.shell));
		}

		while (!this.shell.isDisposed()) {
			if (!this.display.readAndDispatch()) {
				this.display.sleep();
			}
		}
		region.dispose();
		this.display.dispose();
	}

	/**
	 * Builds the rounded rectangular region.
	 *
	 * @param bounds the bounds
	 * @param radius the radius
	 * @return the region
	 */
	private Region buildRoundedRectangularRegion(Rectangle bounds, int radius) {
		Region region = new Region();
		Point p;
		region.add(bounds.x + radius, bounds.y + radius, bounds.width,
				bounds.height);
		// Top
		p = new Point(bounds.x + radius, bounds.y);
		region.add(new Rectangle(p.x, p.y, bounds.width, radius));

		// Right
		p = new Point(bounds.x + bounds.width + radius, bounds.y + radius);
		region.add(new Rectangle(p.x, p.y, radius, bounds.height));

		// Bottom
		p = new Point(bounds.x + radius, bounds.y + bounds.height + radius);
		region.add(new Rectangle(p.x, p.y, bounds.width, radius));

		// Left
		p = new Point(bounds.x, bounds.y + radius);
		region.add(new Rectangle(p.x, p.y, radius, bounds.height));

		// Corners
		p = new Point(bounds.x + radius, bounds.y + radius);
		// TopLeft
		region.add(circle(radius, p.x, p.y));
		// TopRight
		region.add(circle(radius, p.x + bounds.width, p.y));
		// BottomRight
		region.add(circle(radius, p.x + bounds.width, p.y + bounds.height));
		// BottomLeft
		region.add(circle(radius, p.x, p.y + bounds.height));
		return region;
	}

	/**
	 * Circle.
	 *
	 * @param r the r
	 * @param offsetX the offset x
	 * @param offsetY the offset y
	 * @return the path
	 */
	private int[] circle(int r, int offsetX, int offsetY) {
		final int[] polygon = new int[8 * r + 4];
		//x^2 + y^2 = r^2
		int x;
		int y;
		for (int i = 0; i < 2 * r + 1; i++) {
			x = i - r;
			y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	private Point getLocation() {
		// TODO handle preference (TOP/BOTTOM, LEFT/RIGHT)
		// Top Left
		Monitor monitor = this.display.getPrimaryMonitor();
		Rectangle clientArea = monitor.getClientArea();
		Point result = new Point(0, 0);
		result.x = (clientArea.x + clientArea.width)
				- (this.shell.getSize().x + HORIZONTAL_PADDING + 2 * RADIUS);
		result.y = (clientArea.y) + (this.shell.getSize().y + VERTICAL_PADDING);
		return result;
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

		// Shell
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = RADIUS;
		layout.marginWidth = RADIUS;
		this.shell.setLayout(layout);
		if (colors != null) {
			this.shell.setBackground(colors.getBackgroundColor());
			this.shell.setForeground(colors.getForegroundColor());
		}
		this.shell.setCursor(this.display.getSystemCursor(SWT.CURSOR_HAND));

		// Icon
		this.lblIcon = new Label(this.shell, SWT.NONE);
		Image image = Activator.getImage(this.notification.getImageUrl());
		this.lblIcon.setImage(image);
		if (colors != null) {
			this.lblIcon.setBackground(colors.getBackgroundColor());
		}
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
		gridData.verticalSpan = 2;
		this.lblIcon.setLayoutData(gridData);

		// Title
		this.lblTitle = new Label(this.shell, SWT.WRAP);
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
		this.lblMessage = new Label(this.shell, SWT.WRAP);
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
