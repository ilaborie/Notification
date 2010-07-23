package org.ilaborie.osgi.notification.swt.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
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
		// TODO retrieve a LogService for logging exception
		Image result = images.get(imagePath);
		if (result == null) {
			// Lookup image into icons folder
			URL url = context.getBundle().getEntry(imagePath);
			if (url != null) {
				InputStream in = null;
				try {
					in = new BufferedInputStream(url.openStream());
					result = new Image(Display.getDefault(), new ImageData(in));
				} catch (SWTException e) {
					if (e.code != SWT.ERROR_INVALID_IMAGE) {
						throw e;
						// fall through otherwise
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						// Cannot do more
					}
				}
			}

			images.put(imagePath, result);
		}
		return result;
	}

}
