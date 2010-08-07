/**
 * 
 */
package org.ilaborie.osgi.notification.jface;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.NotificationEvent;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;
import org.ilaborie.osgi.notification.jface.internal.Activator;

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
	private void showNotification(INotification notification) {
		final Image titleImage = this.getTitleImage(notification);

		NotificationPopup notificationPopup = new NotificationPopup(
				Display.getCurrent(), notification) {
			@Override
			protected Image getNotificationImage(INotification notification) {
				return titleImage;
			}
		};
		if (notificationPopup.open() == Window.OK) {
			this.fireNotificationActivatedEvent(new NotificationEvent(this,
					notification));
		}
	}

	/**
	 * Gets the title image.
	 *
	 * @param notification the notification
	 * @return the title image
	 */
	private Image getTitleImage(INotification notification) {
		return Activator.getImage("icons/handbell_16.png"); //$NON-NLS-1$
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
	private void fireNotificationActivatedEvent(NotificationEvent event) {
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
