/**
 * 
 */
package org.ilaborie.osgi.notification;

import java.net.URL;
import java.util.Map;

/**
 * The Interface INotification.
 *
 * @author igor
 */
public interface INotification {

	/** The default expire time. */
	int EXPIRE_TIME_DEFAULT = 5 * 1000; // 5s

	/** The default urgency. */
	Urgency URGENCY_DEFAULT = Urgency.NORMAL;

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

	/**
	 * Gets the image url.
	 *
	 * @return the image url
	 */
	URL getImageUrl();

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	Object getCategory();

	/**
	 * Gets the urgency.
	 *
	 * @return the urgency
	 */
	Urgency getUrgency();

	/**
	 * Gets the expire time.
	 *
	 * @return the expire time
	 */
	int getExpireTime();

	/**
	 * Gets the extra data.
	 *
	 * @return the extra data
	 */
	Map<String, Object> getExtraData();

}
