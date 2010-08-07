/**
 * 
 */
package org.ilaborie.osgi.notification.sysout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.NotificationEvent;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;

/**
 * The Class SysoutNotificationService.
 *
 * @author igor
 */
public class SysoutNotificationService implements INotificationService {

	/** The listeners. */
	private final Set<INotificationListener> listeners = new LinkedHashSet<INotificationListener>();

	/**
	 * Instantiates a new sysout notification service.
	 */
	public SysoutNotificationService() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationService#show(org.ilaborie.osgi.notification.INotification)
	 */
	@Override
	public void show(INotification notification) {
		if (notification == null) {
			throw new IllegalArgumentException(
					"Notification shoudn't being null !"); //$NON-NLS-1$
		}
		NotificationEvent event = new NotificationEvent(this, notification);
		if (this.fireBeforeNotificationEvent(event)) {
			String message;
			if (notification.getTitle() != null
					&& notification.getCategory() != null) {
				message = String.format(
						"<%1$s> %2$s - %3$s", notification.getCategory(), //$NON-NLS-1$
						notification.getTitle(), notification.getMessage());
			} else if (notification.getCategory() != null) {
				assert notification.getTitle() == null;
				message = String.format(
						"<%1$s> %2$s", notification.getCategory(), //$NON-NLS-1$
						notification.getMessage());
			} else if (notification.getTitle() != null) {
				assert notification.getCategory() == null;
				message = String.format("%1$s - %2$s", notification.getTitle(), //$NON-NLS-1$
						notification.getMessage());
			} else {
				assert notification.getCategory() == null;
				assert notification.getTitle() == null;
				message = notification.getMessage();
			}
			System.out.println(message);

			event = new NotificationEvent(this, notification);
			this.fireAfterNotificationEvent(event);
		}
	}

	/**
	 * Fire before notification event.
	 *
	 * @param event the event
	 * @return true, if successful
	 */
	private boolean fireBeforeNotificationEvent(NotificationEvent event) {
		boolean result = true;
		assert event != null;
		List<INotificationListener> allListeners = new ArrayList<INotificationListener>(
				this.listeners);
		for (INotificationListener listener : allListeners) {
			try {
				result = listener.beforeNotification(event);
				if (!result) {
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Fire after notification event.
	 *
	 * @param event the event
	 */
	private void fireAfterNotificationEvent(NotificationEvent event) {
		assert event != null;
		List<INotificationListener> allListeners = new ArrayList<INotificationListener>(
				this.listeners);
		for (INotificationListener listener : allListeners) {
			try {
				listener.afterNotification(event);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationService#addNotificationListener(org.ilaborie.osgi.notification.INotificationListener)
	 */
	@Override
	public void addNotificationListener(INotificationListener listener) {
		if (listener != null) {
			synchronized (this.listeners) {
				this.listeners.add(listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotificationService#removeNotificationListener(org.ilaborie.osgi.notification.INotificationListener)
	 */
	@Override
	public void removeNotificationListener(INotificationListener listener) {
		if (listener != null) {
			synchronized (this.listeners) {
				this.listeners.remove(listener);
			}
		}
	}

}
