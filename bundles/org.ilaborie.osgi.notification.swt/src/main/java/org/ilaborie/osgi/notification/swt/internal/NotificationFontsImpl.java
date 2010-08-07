/**
 * 
 */
package org.ilaborie.osgi.notification.swt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.ilaborie.osgi.notification.swt.INotificationFonts;

/**
 * This default implementation use System Font.
 * @author igor
 */
public class NotificationFontsImpl implements INotificationFonts {

	/**
	 * The Enum FontKey.
	 */
	private enum FontKey {
		/** The TITL e_ foreground. */
		TITLE_FONT,
		/** The MESSAG e_ foreground. */
		MESSAGE_FONT;
	}

	// Attributes
	/** The colors. */
	private final Map<FontKey, Font> colors = new HashMap<FontKey, Font>();

	// Constructor
	/**
	 * Instantiates a new notification colors impl.
	 */
	public NotificationFontsImpl() {
		super();
	}

	// Methods

	/**
	 * Dispose font.
	 */
	public void dispose() {
		for (Font color : this.colors.values()) {
			if (!color.isDisposed()) {
				color.dispose();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationFonts#getTitleFont()
	 */
	@Override
	public Font getTitleFont() {
		Font result = this.colors.get(FontKey.TITLE_FONT);
		if (result == null) {
			Display display = Display.getDefault();
			Font systemFont = display.getSystemFont();
			// Set bold
			FontData[] boldData = this
					.getModifiedFontData(systemFont, SWT.BOLD);
			result = new Font(display, boldData);
			this.colors.put(FontKey.TITLE_FONT, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.swt.INotificationFonts#getMessageFont()
	 */
	@Override
	public Font getMessageFont() {
		Font result = this.colors.get(FontKey.MESSAGE_FONT);
		if (result == null) {
			Display display = Display.getDefault();
			Font systemFont = display.getSystemFont();
			FontData[] boldData = this.getModifiedFontData(systemFont,
					SWT.NORMAL);
			result = new Font(display, boldData);
			this.colors.put(FontKey.MESSAGE_FONT, result);
		}
		return result;
	}

	/**
	 * Get a version of the base font data with the specified
	 * style.
	 *
	 * @param baseFont the base font
	 * @param style the new style
	 * @return the font data with the style {@link FontData#FontData(String, int, int)}
	 * @see SWT#ITALIC
	 * @see SWT#NORMAL
	 * @see SWT#BOLD
	 */
	private FontData[] getModifiedFontData(Font baseFont, int style) {
		FontData[] baseData = baseFont.getFontData();
		FontData[] styleData = new FontData[baseData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = baseData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(),
					base.getStyle() | style);
		}

		return styleData;
	}
}
