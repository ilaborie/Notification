/**
 * 
 */
package org.ilaborie.osgi.notification;

/**
 * The Interface INotificationService.
 *
 * @author igor
 */
public interface INotificationService {

	/**
	 * Show the notification.
	 *
	 * @param notification the notification
	 */
	void show(INotification notification);

	/**
	 * Adds the notification listener.
	 *
	 * @param listener the listener
	 */
	void addNotificationListener(INotificationListener listener);

	/**
	 * Removes the notification listener.
	 *
	 * @param listener the listener
	 */
	void removeNotificationListener(INotificationListener listener);
}
