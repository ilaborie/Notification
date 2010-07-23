/**
 * 
 */
package org.ilaborie.osgi.notification.swt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.INotificationEvent;
import org.ilaborie.osgi.notification.INotificationListener;
import org.ilaborie.osgi.notification.INotificationService;
import org.ilaborie.osgi.notification.swt.internal.Activator;

/**
 * The Class JFaceNotificationService.
 *
 * @author igor
 */
public class SwtNotificationService implements INotificationService {

	/** The listeners. */
	private final Set<INotificationListener> listeners = new LinkedHashSet<INotificationListener>();

	/** The icon provider. */
	private INotificationIconProvider iconProvider = null;

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
		if (this.fireBeforeNotificationEvent(new INotificationEvent(this,
				notification))) {

			this.showNotification(notification);

			this.fireAfterNotificationEvent(new INotificationEvent(this,
					notification));
		}
	}

	/**
	 * Show notification.
	 *
	 * @param notification the notification
	 */
	private void showNotification(INotification notification) {
		Display display = Display.getDefault();
		Shell activeShell = display.getActiveShell();
		Shell shell = NotifierDialog.notify(notification.getTitle(),
				notification.getMessage(), this.getTitleImage(notification));

		if (activeShell == null && shell != null) {
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}

	/**
	 * Gets the title image.
	 *
	 * @param notification the notification
	 * @return the title image
	 */
	private Image getTitleImage(INotification notification) {
		Image result = null;
		if (this.getIconProvider() != null) {
			result = this.getIconProvider().getIcon(notification);
		}
		if (result == null) {
			// Use a default Image
			result = Activator.getImage("icons/bell.png"); //$NON-NLS-1$
		}
		return result;
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

	/**
	 * Gets the icon provider.
	 *
	 * @return the iconProvider
	 */
	public INotificationIconProvider getIconProvider() {
		return this.iconProvider;
	}

	/**
	 * Sets the icon provider.
	 *
	 * @param iconProvider the iconProvider to set
	 */
	public void setIconProvider(INotificationIconProvider iconProvider) {
		this.iconProvider = iconProvider;
	}

}
