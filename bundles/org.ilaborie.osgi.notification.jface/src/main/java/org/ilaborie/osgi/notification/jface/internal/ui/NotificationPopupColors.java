/*
 * 
 */
package org.ilaborie.osgi.notification.jface.internal.ui;

import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

// TODO: Auto-generated Javadoc
/**
 * Helper Class to create the colors for the {@link AbstractNotificationPopup}.
 * <p>
 * Note: Copied from FormColors of UI Forms.
 * </p>
 * 
 * @author Benjamin Pasero (initial contribution from RSSOwl, see bug 177974)
 * @author Mik Kersten
 */
public class NotificationPopupColors {

	/** The display. */
	private final Display display;

	/** The title text. */
	private Color titleText;

	/** The gradient begin. */
	private Color gradientBegin;

	/** The gradient end. */
	private Color gradientEnd;

	/** The border. */
	private Color border;

	/** The resource manager. */
	private final ResourceManager resourceManager;

	/**
	 * Instantiates a new notification popup colors.
	 *
	 * @param display the display
	 * @param resourceManager the resource manager
	 */
	public NotificationPopupColors(Display display,
			ResourceManager resourceManager) {
		this.display = display;
		this.resourceManager = resourceManager;

		this.createColors();
	}

	/**
	 * Creates the colors.
	 */
	private void createColors() {
		this.createBorderColor();
		this.createGradientColors();
		// previously used SWT.COLOR_TITLE_INACTIVE_FOREGROUND, but too light on Windows XP
		this.titleText = this.getColor(this.resourceManager,
				this.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
	}

	/**
	 * Gets the gradient begin.
	 *
	 * @return the gradient begin
	 */
	public Color getGradientBegin() {
		return this.gradientBegin;
	}

	/**
	 * Gets the gradient end.
	 *
	 * @return the gradient end
	 */
	public Color getGradientEnd() {
		return this.gradientEnd;
	}

	/**
	 * Gets the border.
	 *
	 * @return the border
	 */
	public Color getBorder() {
		return this.border;
	}

	/**
	 * Gets the title text.
	 *
	 * @return the title text
	 */
	public Color getTitleText() {
		return this.titleText;
	}

	/**
	 * Creates the border color.
	 */
	private void createBorderColor() {
		RGB tbBorder = this.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		final RGB bg = this.getImpliedBackground().getRGB();

		// Group 1
		// Rule: If at least 2 of the RGB values are equal to or between 180 and
		// 255, then apply specified opacity for Group 1
		// Examples: Vista, XP Silver, Wn High Con #2
		// Keyline = TITLE_BACKGROUND @ 70% Opacity over LIST_BACKGROUND
		if (this.testTwoPrimaryColors(tbBorder, 179, 256)) {
			tbBorder = this.blend(tbBorder, bg, 70);
		} else if (this.testTwoPrimaryColors(tbBorder, 120, 180)) {
			tbBorder = this.blend(tbBorder, bg, 50);
		} else {
			tbBorder = this.blend(tbBorder, bg, 30);
		}

		this.border = this.getColor(this.resourceManager, tbBorder);
	}

	/**
	 * Creates the gradient colors.
	 */
	private void createGradientColors() {
		final RGB titleBg = this.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		final Color bgColor = this.getImpliedBackground();
		final RGB bg = bgColor.getRGB();
		RGB bottom, top;

		// Group 1
		// Rule: If at least 2 of the RGB values are equal to or between 180 and
		// 255, then apply specified opacity for Group 1
		// Examples: Vista, XP Silver, Wn High Con #2
		// Gradient Bottom = TITLE_BACKGROUND @ 30% Opacity over LIST_BACKGROUND
		// Gradient Top = TITLE BACKGROUND @ 0% Opacity over LIST_BACKGROUND
		if (this.testTwoPrimaryColors(titleBg, 179, 256)) {
			bottom = this.blend(titleBg, bg, 30);
			top = bg;
		}

		// Group 2
		// Rule: If at least 2 of the RGB values are equal to or between 121 and
		// 179, then apply specified opacity for Group 2
		// Examples: XP Olive, OSX Graphite, Linux GTK, Wn High Con Black
		// Gradient Bottom = TITLE_BACKGROUND @ 20% Opacity over LIST_BACKGROUND
		// Gradient Top = TITLE BACKGROUND @ 0% Opacity over LIST_BACKGROUND
		else if (this.testTwoPrimaryColors(titleBg, 120, 180)) {
			bottom = this.blend(titleBg, bg, 20);
			top = bg;
		}

		// Group 3
		// Rule: If at least 2 of the RGB values are equal to or between 0 and
		// 120, then apply specified opacity for Group 3
		// Examples: XP Default, Wn Classic Standard, Wn Marine, Wn Plum, OSX
		// Aqua, Wn High Con White, Wn High Con #1
		// Gradient Bottom = TITLE_BACKGROUND @ 10% Opacity over LIST_BACKGROUND
		// Gradient Top = TITLE BACKGROUND @ 0% Opacity over LIST_BACKGROUND
		else {
			bottom = this.blend(titleBg, bg, 10);
			top = bg;
		}

		this.gradientBegin = this.getColor(this.resourceManager, top);
		this.gradientEnd = this.getColor(this.resourceManager, bottom);
	}

	/**
	 * Blend.
	 *
	 * @param c1 the c1
	 * @param c2 the c2
	 * @param ratio the ratio
	 * @return the rGB
	 */
	private RGB blend(RGB c1, RGB c2, int ratio) {
		final int r = this.blend(c1.red, c2.red, ratio);
		final int g = this.blend(c1.green, c2.green, ratio);
		final int b = this.blend(c1.blue, c2.blue, ratio);
		return new RGB(r, g, b);
	}

	/**
	 * Blend.
	 *
	 * @param v1 the v1
	 * @param v2 the v2
	 * @param ratio the ratio
	 * @return the int
	 */
	private int blend(int v1, int v2, int ratio) {
		final int b = (ratio * v1 + (100 - ratio) * v2) / 100;
		return Math.min(255, b);
	}

	/**
	 * Test two primary colors.
	 *
	 * @param rgb the rgb
	 * @param from the from
	 * @param to the to
	 * @return true, if successful
	 */
	private boolean testTwoPrimaryColors(RGB rgb, int from, int to) {
		int total = 0;
		if (this.testPrimaryColor(rgb.red, from, to)) {
			total++;
		}
		if (this.testPrimaryColor(rgb.green, from, to)) {
			total++;
		}
		if (this.testPrimaryColor(rgb.blue, from, to)) {
			total++;
		}
		return total >= 2;
	}

	/**
	 * Test primary color.
	 *
	 * @param value the value
	 * @param from the from
	 * @param to the to
	 * @return true, if successful
	 */
	private boolean testPrimaryColor(int value, int from, int to) {
		return (value > from) && (value < to);
	}

	/**
	 * Gets the system color.
	 *
	 * @param code the code
	 * @return the system color
	 */
	private RGB getSystemColor(int code) {
		return this.getDisplay().getSystemColor(code).getRGB();
	}

	/**
	 * Gets the implied background.
	 *
	 * @return the implied background
	 */
	private Color getImpliedBackground() {
		return this.display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	/**
	 * Gets the display.
	 *
	 * @return the display
	 */
	private Display getDisplay() {
		return this.display;
	}

	/**
	 * Gets the color.
	 *
	 * @param manager the manager
	 * @param rgb the rgb
	 * @return the color
	 */
	private Color getColor(ResourceManager manager, RGB rgb) {
		try {
			return manager.createColor(rgb);
		} catch (final DeviceResourceException e) {
			return manager.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}
	}
}