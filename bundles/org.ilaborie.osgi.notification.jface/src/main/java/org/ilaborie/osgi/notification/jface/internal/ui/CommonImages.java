/**
 * 
 */
package org.ilaborie.osgi.notification.jface.internal.ui;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.ilaborie.osgi.notification.jface.internal.Activator;

/**
 * The Class CommonImages.
 *
 * @author igor
 */
public class CommonImages {

	/** The registry. */
	private static ImageRegistry registry = new ImageRegistry(
			Display.getDefault());

	/** The Constant HANDBELL_16. */
	public static final String HANDBELL_16 = "/icons/handbell_16.png"; //$NON-NLS-1$

	/** The Constant NOTIFICATION_CLOSE. */
	public static final String NOTIFICATION_CLOSE = "/icons/notification-close.gif"; //$NON-NLS-1$

	/** The Constant NOTIFICATION_CLOSE_HOVER. */
	public static final String NOTIFICATION_CLOSE_HOVER = "/icons/notification-close-active.gif"; //$NON-NLS-1$

	/**
	 * Gets the image.
	 *
	 * @param imageKey the image key
	 * @return the image
	 */
	public static Image getImage(String imageKey) {
		Image image = registry.get(imageKey);
		if (image == null) {
			ImageDescriptor descriptor = null;
			final URL url = Activator.getContext().getBundle()
					.getEntry(imageKey);
			if (url != null) {
				descriptor = ImageDescriptor.createFromURL(url);
			} else {
				descriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			if (descriptor != null) {
				image = descriptor.createImage();
				registry.put(imageKey, image);
			}
		}
		return image;
	}

}
