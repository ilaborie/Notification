/**
 * 
 */
package org.ilaborie.osgi.notification.jface;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.INotificationEvent;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;

/**
 * The Class JFaceNotificationService.
 *
 * @author igor
 */
public class JFaceNotificationService implements INotificationService {

	/** The listeners. */
	private final Set<INotificationListener> listeners = new LinkedHashSet<INotificationListener>();

	/**
	 * Instantiates a new j face notification service.
	 */
	public JFaceNotificationService() {
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
		INotificationEvent event = new INotificationEvent(this, notification);
		if (this.fireBeforeNotificationEvent(event)) {

			this.showNotification(notification);

			event = new INotificationEvent(this, notification);
			this.fireAfterNotificationEvent(event);
		}
	}

	/**
	 * Show notification.
	 *
	 * @param notification the notification
	 */
	private void showNotification(INotification notification) {
		// TODO Auto-generated method stub
	}

	/**
	 * Fire before notification event.
	 *
	 * @param event the event
	 * @return true, if successful
	 */
	private boolean fireBeforeNotificationEvent(INotificationEvent event) {
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
	private void fireAfterNotificationEvent(INotificationEvent event) {
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
