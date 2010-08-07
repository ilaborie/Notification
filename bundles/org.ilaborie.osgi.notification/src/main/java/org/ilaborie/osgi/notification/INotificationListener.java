/**
 * 
 */
package org.ilaborie.osgi.notification;

/**
 * The listener interface for receiving INotification events.
 * The class that is interested in processing a INotificationEvent implements 
 * this interface, and the object created with that class is registered with a
 * component using the component's <code>addINotificationListener<code> method.
 * When the INotification event occurs, that object's appropriate method is 
 * invoked.
 *
 * @author igor
 */
public interface INotificationListener {

	/**
	 * Before notification.
	 *
	 * @param event the event
	 * @return true, if notification should been shown
	 */
	boolean beforeNotification(NotificationEvent event);

	/**
	 * On notification activated.
	 *
	 * @param event the event
	 */
	void onNotificationActivated(NotificationEvent event);

	/**
	 * After notification.
	 *
	 * @param event the event
	 */
	void afterNotification(NotificationEvent event);

}
