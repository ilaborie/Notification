/**
 * 
 */
package org.ilaborie.osgi.notification.swt;

import org.eclipse.swt.graphics.Image;
import org.ilaborie.osgi.notification.INotification;

/**
 * The Interface INotificationIconProvider.
 *
 * @author igor
 */
public interface INotificationIconProvider {

	/**
	 * Gets the icon.
	 *
	 * @param notification the notification
	 * @return the icon
	 */
	Image getIcon(INotification notification);

}
