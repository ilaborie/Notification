/**
 * 
 */
package org.ilaborie.osgi.notification.tools.console;

import org.ilaborie.osgi.notification.INotification;

/**
 * The Class BasicNotification.
 *
 * @author igor
 */
public class BasicNotification implements INotification {

	/** The message. */
	private final String message;

	/** The title. */
	private final String title;

	/**
	 * Instantiates a new basic notification.
	 *
	 * @param message the message
	 * @param title the title
	 */
	public BasicNotification(String message, String title) {
		super();
		if (message == null) {
			throw new IllegalArgumentException("Message shouldn't being null"); //$NON-NLS-1$
		}
		this.message = message;
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getType()
	 */
	@Override
	public Object getType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getTitle()
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getMessage()
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

}
