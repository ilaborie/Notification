/**
 * 
 */
package org.ilaborie.osgi.notification.impl;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.Urgency;

/**
 * The Class NotificationImpl.
 *
 * @author igor
 */
public class NotificationImpl implements INotification {

	// Attributes
	/** The title. */
	private String title;

	/** The message. */
	private String message;

	/** The image url. */
	private URL imageUrl;

	/** The category. */
	private Object category;

	/** The urgency. */
	private Urgency urgency = URGENCY_DEFAULT;

	/** The expire time. */
	private int expireTime = EXPIRE_TIME_DEFAULT;

	/** The data. */
	private final Map<String, Object> data = new HashMap<String, Object>();

	// Constructors
	/**
	 * Instantiates a new notification impl.
	 */
	public NotificationImpl() {
		super();
	}

	/**
	 * Instantiates a new notification impl.
	 *
	 * @param message the message
	 */
	public NotificationImpl(String message) {
		this();
		this.message = message;
	}

	/**
	 * Instantiates a new notification impl.
	 *
	 * @param title the title
	 * @param message the message
	 */
	public NotificationImpl(String title, String message) {
		this(message);
		this.title = title;
	}

	/**
	 * Instantiates a new notification impl.
	 *
	 * @param title the title
	 * @param message the message
	 * @param imageUrl the image url
	 */
	public NotificationImpl(String title, String message, URL imageUrl) {
		this(title, message);
		this.imageUrl = imageUrl;
	}

	// Methods
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("NotificationImpl [title=%s, message=%s]", //$NON-NLS-1$
				this.title, this.message);
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

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getImageUrl()
	 */
	@Override
	public URL getImageUrl() {
		return this.imageUrl;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getCategory()
	 */
	@Override
	public Object getCategory() {
		return this.category;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getUrgency()
	 */
	@Override
	public Urgency getUrgency() {
		return this.urgency;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getExpireTime()
	 */
	@Override
	public int getExpireTime() {
		return this.expireTime;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.osgi.notification.INotification#getExtraData()
	 */
	@Override
	public Map<String, Object> getExtraData() {
		return Collections.unmodifiableMap(this.data);
	}

	// Getters & Setters

	/**
	 * Adds the extra data.
	 *
	 * @param key the key
	 * @param newData the data
	 * @return the object
	 */
	public Object addExtraData(String key, Object newData) {
		return this.data.put(key, newData);
	}

	/**
	 * Gets the extra data keys.
	 *
	 * @return the extra data keys
	 */
	public Set<String> getExtraDataKeys() {
		return this.data.keySet();
	}

	/**
	 * Removes the extra data.
	 *
	 * @param key the key
	 * @return the object
	 */
	public Object removeExtraData(String key) {
		return this.data.remove(key);
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the new image url
	 */
	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(Object category) {
		this.category = category;
	}

	/**
	 * Sets the urgency.
	 *
	 * @param urgency the urgency to set
	 */
	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}

	/**
	 * Sets the expire time.
	 *
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
}
