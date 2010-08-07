/**
 * 
 */
package org.ilaborie.osgi.notification.swt;

import org.eclipse.swt.graphics.Color;

/**
 * The Interface INotificationColors.
 * @author igor
 */
public interface INotificationColors {

	/**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
	Color getBackgroundColor();

	/**
	 * Gets the foreground color.
	 *
	 * @return the foreground color
	 */
	Color getForegroundColor();

	/**
	 * Gets the title color.
	 *
	 * @return the title color
	 */
	Color getTitleColor();

	/**
	 * Gets the message color.
	 *
	 * @return the message color
	 */
	Color getMessageColor();

}
