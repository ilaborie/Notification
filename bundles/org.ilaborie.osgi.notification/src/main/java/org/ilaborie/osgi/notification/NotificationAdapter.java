/**
 * 
 */
package org.ilaborie.osgi.notification;

/**
 * The Class NotificationAdapter.
 *
 * @author igor
 */
public class NotificationAdapter implements INotificationListener {

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationListener#beforeNotification(org.ilaborie.osgi.notification.INotificationEvent)
	 */
	@Override
	public boolean beforeNotification(INotificationEvent event) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationListener#onNotificationActivated(org.ilaborie.osgi.notification.INotificationEvent)
	 */
	@Override
	public void onNotificationActivated(INotificationEvent event) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationListener#afterNotification(org.ilaborie.osgi.notification.INotificationEvent)
	 */
	@Override
	public void afterNotification(INotificationEvent event) {
		// Do nothing
	}

}
