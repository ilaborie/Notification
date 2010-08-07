/**
 * 
 */
package org.ilaborie.osgi.notification;

/**
 * The Interface INotificationEvent.
 *
 * @author igor
 */
public final class NotificationEvent {

	/** The source. */
	private final Object source;

	/** The timestamp. */
	private final long timestamp;

	/** The notification. */
	private final INotification notification;

	/** The detail. */
	private final Object detail;

	/**
	 * Instantiates a new INotificationEvent.
	 *
	 * @param source the source. Shouldn't being <code>null</code>
	 * @param notification the notification. Shouldn't being <code>null</code>
	 * @param detail the detail. Could be null if no detail associated to event
	 */
	public NotificationEvent(Object source, INotification notification,
			Object detail) {
		super();
		if (source == null || notification == null) {
			throw new IllegalArgumentException(
					"Could not able to create the INotificationEvent : source and notification shouldn't being null !"); //$NON-NLS-1$
		}
		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.notification = notification;
		this.detail = detail;
	}

	/**
	 * Instantiates a new INotificationEvent.
	 *
	 * @param source the source. Shouldn't being <code>null</code>
	 * @param notification the notification. Shouldn't being <code>null</code>
	 */
	public NotificationEvent(Object source, INotification notification) {
		this(source, notification, null);
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public Object getSource() {
		return this.source;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Gets the notification.
	 *
	 * @return the notification
	 */
	public INotification getNotification() {
		return this.notification;
	}

	/**
	 * Gets the detail.
	 *
	 * @return the detail
	 */
	public Object getDetail() {
		return this.detail;
	}

}
