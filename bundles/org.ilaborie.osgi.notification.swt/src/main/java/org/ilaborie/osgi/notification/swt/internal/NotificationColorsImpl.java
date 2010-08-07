/**
 * 
 */
package org.ilaborie.osgi.notification.swt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.ilaborie.osgi.notification.swt.INotificationColors;

/**
 * The Class NotificationColorsImpl.
 * This default implementation use System colors.
 * @author igor
 */
public class NotificationColorsImpl implements INotificationColors {

	/**
	 * The Enum ColorKey.
	 */
	private enum ColorKey {
		/** The BACKGROUND. */
		BACKGROUND,
		/** The FOREGROUND. */
		FOREGROUND,
		/** The TITL e_ foreground. */
		TITLE_FOREGROUND,
		/** The MESSAG e_ foreground. */
		MESSAGE_FOREGROUND;
	}

	// Attributes
	/** The colors. */
	private final Map<ColorKey, Color> colors = new HashMap<ColorKey, Color>();

	// Constructor
	/**
	 * Instantiates a new notification colors impl.
	 */
	public NotificationColorsImpl() {
		super();
	}

	// Methods

	/**
	 * Dispose colors.
	 */
	public void dispose() {
		for (Color color : this.colors.values()) {
			if (!color.isDisposed()) {
				color.dispose();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationColors#getNotificationBackgroundColor()
	 */
	@Override
	public Color getBackgroundColor() {
		Color result = this.colors.get(ColorKey.BACKGROUND);
		if (result == null) {
			Display display = Display.getDefault();
			Color systemColor = display
					.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
			result = new Color(display, systemColor.getRGB());
			this.colors.put(ColorKey.BACKGROUND, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationColors#getForegroundColor()
	 */
	@Override
	public Color getForegroundColor() {
		Color result = this.colors.get(ColorKey.FOREGROUND);
		if (result == null) {
			Display display = Display.getDefault();
			Color systemColor = display
					.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
			result = new Color(display, systemColor.getRGB());
			this.colors.put(ColorKey.FOREGROUND, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationColors#getTitleColor()
	 */
	@Override
	public Color getTitleColor() {
		Color result = this.colors.get(ColorKey.TITLE_FOREGROUND);
		if (result == null) {
			Display display = Display.getDefault();
			Color systemColor = display
					.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
			result = new Color(display, systemColor.getRGB());
			this.colors.put(ColorKey.TITLE_FOREGROUND, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationColors#getMessageColor()
	 */
	@Override
	public Color getMessageColor() {
		Color result = this.colors.get(ColorKey.MESSAGE_FOREGROUND);
		if (result == null) {
			Display display = Display.getDefault();
			Color systemColor = display
					.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
			result = new Color(display, systemColor.getRGB());
			this.colors.put(ColorKey.MESSAGE_FOREGROUND, result);
		}
		return result;
	}
}
