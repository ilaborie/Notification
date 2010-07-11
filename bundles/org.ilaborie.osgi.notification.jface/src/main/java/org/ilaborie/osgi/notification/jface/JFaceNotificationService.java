/**
 * 
 */
package org.ilaborie.osgi.notification.jface;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.INotificationEvent;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;
import org.ilaborie.osgi.notification.jface.internal.ui.AbstractNotificationPopup;
import org.ilaborie.osgi.notification.jface.internal.ui.CommonImages;

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
		// TODO notify selection

		final String title = notification.getTitle() != null ? notification
				.getTitle() : "Notification";
		final String message = notification.getMessage();
		final Image titleImage = this.getTitleImage(notification);

		AbstractNotificationPopup notificationPopup = new AbstractNotificationPopup(
				Display.getCurrent()) {
			@Override
			protected void createContentArea(Composite parent) {
				Label lbl = new Label(parent, SWT.WRAP);
				lbl.setText(message);
			}

			@Override
			public Image getTitleImage(int maximumHeight) {
				return titleImage;
			}

			@Override
			public String getTitle() {
				return title;
			}
		};
		notificationPopup.open();

	}

	/**
	 * Gets the title image.
	 *
	 * @param notification the notification
	 * @return the title image
	 */
	private Image getTitleImage(INotification notification) {
		Image img = null;
		// TODO later
		img = CommonImages.getImage(CommonImages.HANDBELL_16);
		return img;
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
	private void fireNotificationActivatedEvent(INotificationEvent event) {
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
