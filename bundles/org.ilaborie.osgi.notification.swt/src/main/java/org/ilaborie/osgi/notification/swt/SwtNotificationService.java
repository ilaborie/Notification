/**
 * 
 */
package org.ilaborie.osgi.notification.swt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;
import org.ilaborie.osgi.notification.NotificationEvent;
import org.ilaborie.osgi.notification.swt.dialog.NotificationDialog;
import org.ilaborie.osgi.notification.swt.internal.Activator;

/**
 * The Class JFaceNotificationService.
 *
 * @author igor
 */
public class SwtNotificationService implements INotificationService {

	/**
	 * The listener interface for receiving notificationSelection events.
	 * The class that is interested in processing a notificationSelection
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addNotificationSelectionListener<code> method. When
	 * the notificationSelection event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @author igor
	 */
	private final class NotificationSelectionListener implements Listener {

		/** The notification. */
		private final INotification notification;

		/** The notification service. */
		private final SwtNotificationService notificationService;

		/**
		 * Instantiates a new notification selection listener.
		 *
		 * @param notificationService the notification service
		 * @param notification the notification
		 */
		NotificationSelectionListener(
				SwtNotificationService notificationService,
				INotification notification) {
			super();
			this.notificationService = notificationService;
			this.notification = notification;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		@Override
		public void handleEvent(Event event) {
			this.notificationService
					.fireNotificationActivatedEvent(new NotificationEvent(
							this.notificationService, this.notification, event));
		}
	}

	/** The listeners. */
	private final Set<INotificationListener> listeners = new LinkedHashSet<INotificationListener>();

	/**
	 * Instantiates a new j face notification service.
	 */
	public SwtNotificationService() {
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
		if (this.fireBeforeNotificationEvent(new NotificationEvent(this,
				notification))) {

			this.showNotification(notification);

			this.fireAfterNotificationEvent(new NotificationEvent(this,
					notification));
		}
	}

	/**
	 * Show notification.
	 *
	 * @param notification the notification
	 */
	private void showNotification(final INotification notification) {
		NotificationDialog dialog = NotificationDialog
				.createNotificationDialog(Activator.getINotificationColors(),
						notification, new NotificationSelectionListener(this,
								notification));
		dialog.show();
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
	void fireNotificationActivatedEvent(NotificationEvent event) {
		assert event != null;
		List<INotificationListener> allListeners = new ArrayList<INotificationListener>(
				this.listeners);
		for (INotificationListener listener : allListeners) {
			try {
				listener.onNotificationActivated(event);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
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
