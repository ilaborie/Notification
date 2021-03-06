package org.ilaborie.osgi.notification.jface.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The Class Activator.
 */
public class Activator implements BundleActivator {

	/** The context. */
	private static BundleContext context;

	/** The images. */
	private static Map<String, Image> images;

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// Initialize Image Map
		images = new HashMap<String, Image>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		// Clean images
		if (images != null) {
			for (Image img : images.values()) {
				img.dispose();
			}
			images = null;
		}
	}

	/**
	 * Gets the image.
	 *
	 * @param imagePath the image path
	 * @return the image
	 */
	public static Image getImage(String imagePath) {
		Image result = images.get(imagePath);
		if (result == null) {
			// Lookup image into icons folder
			URL url = context.getBundle().getEntry(imagePath);
			ImageDescriptor descriptor;
			if (url != null) {
				descriptor = ImageDescriptor.createFromURL(url);
			} else {
				// Oops ! missing image
				descriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			result = descriptor.createImage();
			images.put(imagePath, result);
		}
		return result;
	}

}
