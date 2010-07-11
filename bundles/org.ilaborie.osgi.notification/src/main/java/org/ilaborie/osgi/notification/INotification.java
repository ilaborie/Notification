/**
 * 
 */
package org.ilaborie.osgi.notification;

/**
 * The Interface INotification.
 *
 * @author igor
 */
public interface INotification {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	Object getType();

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	String getTitle();

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	String getMessage();

}
