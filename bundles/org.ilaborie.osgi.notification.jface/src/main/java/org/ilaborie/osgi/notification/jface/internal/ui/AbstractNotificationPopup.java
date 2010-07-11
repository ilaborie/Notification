package org.ilaborie.osgi.notification.jface.internal.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.ilaborie.osgi.notification.jface.internal.ui.SwtUtil.FadeJob;
import org.ilaborie.osgi.notification.jface.internal.ui.SwtUtil.IFadeListener;

// TODO: Auto-generated Javadoc
/**
 * @author Benjamin Pasero
 * @author Mik Kersten
 * @author Steffen Pingel
 */
public abstract class AbstractNotificationPopup extends Window {

	/** The Constant TITLE_HEIGHT. */
	private static final int TITLE_HEIGHT = 24;

	/** The Constant LABEL_JOB_CLOSE. */
	private static final String LABEL_JOB_CLOSE = "Close Notification Job"; //$NON-NLS-1$

	/** The Constant MAX_WIDTH. */
	private static final int MAX_WIDTH = 400;

	/** The Constant MIN_HEIGHT. */
	private static final int MIN_HEIGHT = 100;

	/** The Constant DEFAULT_DELAY_CLOSE. */
	private static final long DEFAULT_DELAY_CLOSE = 8 * 1000;

	/** The Constant PADDING_EDGE. */
	private static final int PADDING_EDGE = 5;

	/** The delay close. */
	private long delayClose = DEFAULT_DELAY_CLOSE;

	/** The resources. */
	protected LocalResourceManager resources;

	/** The color. */
	private NotificationPopupColors color;

	/** The display. */
	private final Display display;

	/** The shell. */
	private Shell shell;

	/** The last used region. */
	private Region lastUsedRegion;

	/** The last used bg image. */
	private Image lastUsedBgImage;

	/** The close job. */
	private final Job closeJob = new Job(LABEL_JOB_CLOSE) {
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			if (!AbstractNotificationPopup.this.display.isDisposed()) {
				AbstractNotificationPopup.this.display
						.asyncExec(new Runnable() {
							@Override
							public void run() {
								final Shell shell = AbstractNotificationPopup.this
										.getShell();
								if ((shell == null) || shell.isDisposed()) {
									return;
								}
								if (AbstractNotificationPopup.this
										.isMouseOver(shell)) {
									AbstractNotificationPopup.this
											.scheduleAutoClose();
									return;
								}

								AbstractNotificationPopup.this.closeFade();
							}

						});
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			return Status.OK_STATUS;
		}
	};

	/** The respect display bounds. */
	private final boolean respectDisplayBounds = true;

	/** The respect monitor bounds. */
	private final boolean respectMonitorBounds = true;

	/** The fade job. */
	private FadeJob fadeJob;

	/** The fading enabled. */
	private boolean fadingEnabled = true;

	/**
	 * Instantiates a new abstract notification popup.
	 *
	 * @param display the display
	 */
	public AbstractNotificationPopup(Display display) {
		this(display, SWT.NO_TRIM | SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
	}

	/**
	 * Instantiates a new abstract notification popup.
	 *
	 * @param display the display
	 * @param style the style
	 */
	public AbstractNotificationPopup(Display display, int style) {
		super(new Shell(display));
		this.setShellStyle(style);

		this.display = display;
		this.resources = new LocalResourceManager(JFaceResources.getResources());
		this.initResources();

		this.closeJob.setSystem(true);
	}

	/**
	 * Checks if is fading enabled.
	 *
	 * @return true, if is fading enabled
	 */
	public boolean isFadingEnabled() {
		return this.fadingEnabled;
	}

	/**
	 * Sets the fading enabled.
	 *
	 * @param fadingEnabled the new fading enabled
	 */
	public void setFadingEnabled(boolean fadingEnabled) {
		this.fadingEnabled = fadingEnabled;
	}

	/**
	 * Gets the popup shell image.
	 *
	 * @param maximumHeight the maximum height
	 * @return the popup shell image
	 */
	public abstract Image getTitleImage(int maximumHeight);

	/**
	 * Override to populate with notifications.
	 * 
	 * @param parent
	 */
	protected void createContentArea(Composite parent) {
		// empty by default
	}

	/**
	 * Override to customize the title bar.
	 *
	 * @param parent the parent
	 */
	protected void createTitleArea(Composite parent) {
		((GridData) parent.getLayoutData()).heightHint = TITLE_HEIGHT;

		final Label titleImageLabel = new Label(parent, SWT.NONE);
		titleImageLabel.setImage(this.getTitleImage(TITLE_HEIGHT));

		final Label titleTextLabel = new Label(parent, SWT.NONE);
		titleTextLabel.setText(this.getTitle());
		titleTextLabel.setFont(JFaceResources.getFontRegistry().getBold(
				JFaceResources.DEFAULT_FONT));
		titleTextLabel.setForeground(this.getTitleForeground());
		//		titleTextLabel.setForeground(color.getTitleText());
		titleTextLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				true));
		titleTextLabel.setCursor(parent.getDisplay().getSystemCursor(
				SWT.CURSOR_HAND));

		final Label button = new Label(parent, SWT.NONE);
		button.setImage(CommonImages.getImage(CommonImages.NOTIFICATION_CLOSE));
		button.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				button.setImage(CommonImages
						.getImage(CommonImages.NOTIFICATION_CLOSE_HOVER));
			}

			@Override
			public void mouseExit(MouseEvent e) {
				button.setImage(CommonImages
						.getImage(CommonImages.NOTIFICATION_CLOSE));
			}
		});
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				AbstractNotificationPopup.this.close();
				AbstractNotificationPopup.this.setReturnCode(CANCEL);
			}

		});
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public abstract String getTitle();

	/**
	 * Gets the title foreground.
	 *
	 * @return the title foreground
	 */
	protected Color getTitleForeground() {
		return this.color.getTitleText();
	}

	/**
	 * Inits the resources.
	 */
	private void initResources() {
		this.color = new NotificationPopupColors(this.display, this.resources);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		this.shell = newShell;
		newShell.setBackground(this.color.getBorder());
	}

	@Override
	public void create() {
		super.create();
		this.addRegion(this.shell);
	}

	/**
	 * Adds the region.
	 *
	 * @param shell the shell
	 */
	private void addRegion(Shell shell) {
		final Region region = new Region();
		final Point s = shell.getSize();

		/* Add entire Shell */
		region.add(0, 0, s.x, s.y);

		/* Subtract Top-Left Corner */
		region.subtract(0, 0, 5, 1);
		region.subtract(0, 1, 3, 1);
		region.subtract(0, 2, 2, 1);
		region.subtract(0, 3, 1, 1);
		region.subtract(0, 4, 1, 1);

		/* Subtract Top-Right Corner */
		region.subtract(s.x - 5, 0, 5, 1);
		region.subtract(s.x - 3, 1, 3, 1);
		region.subtract(s.x - 2, 2, 2, 1);
		region.subtract(s.x - 1, 3, 1, 1);
		region.subtract(s.x - 1, 4, 1, 1);

		/* Subtract Bottom-Left Corner */
		region.subtract(0, s.y, 5, 1);
		region.subtract(0, s.y - 1, 3, 1);
		region.subtract(0, s.y - 2, 2, 1);
		region.subtract(0, s.y - 3, 1, 1);
		region.subtract(0, s.y - 4, 1, 1);

		/* Subtract Bottom-Right Corner */
		region.subtract(s.x - 5, s.y - 0, 5, 1);
		region.subtract(s.x - 3, s.y - 1, 3, 1);
		region.subtract(s.x - 2, s.y - 2, 2, 1);
		region.subtract(s.x - 1, s.y - 3, 1, 1);
		region.subtract(s.x - 1, s.y - 4, 1, 1);

		/* Dispose old first */
		if (shell.getRegion() != null) {
			shell.getRegion().dispose();
		}

		/* Apply Region */
		shell.setRegion(region);

		/* Remember to dispose later */
		this.lastUsedRegion = region;
	}

	/**
	 * Checks if is mouse over.
	 *
	 * @param shell the shell
	 * @return true, if is mouse over
	 */
	private boolean isMouseOver(Shell shell) {
		if (this.display.isDisposed()) {
			return false;
		}
		return shell.getBounds().contains(this.display.getCursorLocation());
	}

	/**
	 * Open.
	 *
	 * @return the int
	 */
	@Override
	public int open() {
		if ((this.shell == null) || this.shell.isDisposed()) {
			this.shell = null;
			this.create();
		}

		this.constrainShellSize();
		this.shell.setLocation(this.fixupDisplayBounds(this.shell.getSize(),
				this.shell.getLocation()));

		if (this.isFadingEnabled()) {
			this.shell.setAlpha(0);
		}
		this.shell.setVisible(true);
		this.fadeJob = SwtUtil.fadeIn(this.shell, new IFadeListener() {
			@Override
			public void faded(Shell shell, int alpha) {
				if (shell.isDisposed()) {
					return;
				}

				if (alpha == 255) {
					AbstractNotificationPopup.this.scheduleAutoClose();
				}
			}
		});

		return Window.OK;
	}

	/**
	 * Schedule auto close.
	 */
	protected void scheduleAutoClose() {
		if (this.delayClose > 0) {
			this.closeJob.schedule(this.delayClose);
		}
	}

	/**
	 * Creates the contents.
	 *
	 * @param parent the parent
	 * @return the control
	 */
	@Override
	protected Control createContents(Composite parent) {
		((GridLayout) parent.getLayout()).marginWidth = 1;
		((GridLayout) parent.getLayout()).marginHeight = 1;

		/* Outer Composite holding the controls */
		final Composite outerCircle = new Composite(parent, SWT.NO_FOCUS);
		outerCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outerCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		outerCircle.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				final Rectangle clArea = outerCircle.getClientArea();
				AbstractNotificationPopup.this.lastUsedBgImage = new Image(
						outerCircle.getDisplay(), clArea.width, clArea.height);
				final GC gc = new GC(
						AbstractNotificationPopup.this.lastUsedBgImage);

				/* Gradient */
				this.drawGradient(gc, clArea);

				/* Fix Region Shape */
				this.fixRegion(gc, clArea);

				gc.dispose();

				final Image oldBGImage = outerCircle.getBackgroundImage();
				outerCircle
						.setBackgroundImage(AbstractNotificationPopup.this.lastUsedBgImage);

				if (oldBGImage != null) {
					oldBGImage.dispose();
				}
			}

			private void drawGradient(GC gc, Rectangle clArea) {
				gc.setForeground(AbstractNotificationPopup.this.color
						.getGradientBegin());
				gc.setBackground(AbstractNotificationPopup.this.color
						.getGradientEnd());
				gc.fillGradientRectangle(clArea.x, clArea.y, clArea.width,
						clArea.height, true);
			}

			private void fixRegion(GC gc, Rectangle clArea) {
				gc.setForeground(AbstractNotificationPopup.this.color
						.getBorder());

				/* Fill Top Left */
				gc.drawPoint(2, 0);
				gc.drawPoint(3, 0);
				gc.drawPoint(1, 1);
				gc.drawPoint(0, 2);
				gc.drawPoint(0, 3);

				/* Fill Top Right */
				gc.drawPoint(clArea.width - 4, 0);
				gc.drawPoint(clArea.width - 3, 0);
				gc.drawPoint(clArea.width - 2, 1);
				gc.drawPoint(clArea.width - 1, 2);
				gc.drawPoint(clArea.width - 1, 3);

				/* Fill Bottom Left */
				gc.drawPoint(2, clArea.height - 0);
				gc.drawPoint(3, clArea.height - 0);
				gc.drawPoint(1, clArea.height - 1);
				gc.drawPoint(0, clArea.height - 2);
				gc.drawPoint(0, clArea.height - 3);

				/* Fill Bottom Right */
				gc.drawPoint(clArea.width - 4, clArea.height - 0);
				gc.drawPoint(clArea.width - 3, clArea.height - 0);
				gc.drawPoint(clArea.width - 2, clArea.height - 1);
				gc.drawPoint(clArea.width - 1, clArea.height - 2);
				gc.drawPoint(clArea.width - 1, clArea.height - 3);
			}
		});

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;

		outerCircle.setLayout(layout);

		/* Title area containing label and close button */
		final Composite titleCircle = new Composite(outerCircle, SWT.NO_FOCUS);
		titleCircle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		titleCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(4, false);
		layout.marginWidth = 3;
		layout.marginHeight = 0;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 3;

		titleCircle.setLayout(layout);

		/* Create Title Area */
		this.createTitleArea(titleCircle);

		/* Outer composite to hold content controlls */
		final Composite outerContentCircle = new Composite(outerCircle,
				SWT.NONE);
		outerContentCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		outerContentCircle.setLayout(layout);
		outerContentCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		outerContentCircle.setBackground(outerCircle.getBackground());

		/* Middle composite to show a 1px black line around the content controls */
		final Composite middleContentCircle = new Composite(outerContentCircle,
				SWT.NO_FOCUS);
		middleContentCircle.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginTop = 1;

		middleContentCircle.setLayout(layout);
		middleContentCircle.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		middleContentCircle.setBackground(this.color.getBorder());

		/* Inner composite containing the content controls */
		final Composite innerContent = new Composite(middleContentCircle,
				SWT.NO_FOCUS);
		innerContent
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		innerContent.setBackgroundMode(SWT.INHERIT_FORCE);

		layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 5;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		innerContent.setLayout(layout);

		innerContent.setBackground(this.shell.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));

		/* Content Area */
		this.createContentArea(innerContent);

		this.setNullBackground(outerCircle);

		return outerCircle;
	}

	/**
	 * Sets the null background.
	 *
	 * @param outerCircle the new null background
	 */
	private void setNullBackground(final Composite outerCircle) {
		for (final Control c : outerCircle.getChildren()) {
			c.setBackground(null);
			if (c instanceof Composite) {
				this.setNullBackground((Composite) c);
			}
		}
	}

	@Override
	protected void initializeBounds() {
		final Rectangle clArea = this.getPrimaryClientArea();
		final Point initialSize = this.shell.computeSize(SWT.DEFAULT,
				SWT.DEFAULT);
		final int height = Math.max(initialSize.y, MIN_HEIGHT);
		final int width = Math.min(initialSize.x, MAX_WIDTH);

		final Point size = new Point(width, height);
		this.shell.setLocation(clArea.width + clArea.x - size.x - PADDING_EDGE,
				clArea.height + clArea.y - size.y - PADDING_EDGE);
		this.shell.setSize(size);
	}

	/**
	 * Gets the primary client area.
	 *
	 * @return the primary client area
	 */
	private Rectangle getPrimaryClientArea() {
		final Monitor primaryMonitor = this.shell.getDisplay()
				.getPrimaryMonitor();
		return (primaryMonitor != null) ? primaryMonitor.getClientArea()
				: this.shell.getDisplay().getClientArea();
	}

	/**
	 * Close fade.
	 */
	public void closeFade() {
		if (this.fadeJob != null) {
			this.fadeJob.cancelAndWait(false);
		}
		this.fadeJob = SwtUtil.fadeOut(this.getShell(), new IFadeListener() {
			@Override
			public void faded(Shell shell, int alpha) {
				if (!shell.isDisposed()) {
					if (alpha == 0) {
						shell.close();
					} else if (AbstractNotificationPopup.this
							.isMouseOver(shell)) {
						if (AbstractNotificationPopup.this.fadeJob != null) {
							AbstractNotificationPopup.this.fadeJob
									.cancelAndWait(false);
						}
						AbstractNotificationPopup.this.fadeJob = SwtUtil
								.fastFadeIn(shell, new IFadeListener() {
									@Override
									public void faded(Shell shell, int alpha) {
										if (shell.isDisposed()) {
											return;
										}

										if (alpha == 255) {
											AbstractNotificationPopup.this
													.scheduleAutoClose();
										}
									}
								});
					}
				}
			}
		});
	}

	@Override
	public boolean close() {
		this.resources.dispose();
		if (this.lastUsedRegion != null) {
			this.lastUsedRegion.dispose();
		}
		if ((this.lastUsedBgImage != null)
				&& !this.lastUsedBgImage.isDisposed()) {
			this.lastUsedBgImage.dispose();
		}
		return super.close();
	}

	/**
	 * Gets the delay close.
	 *
	 * @return the delay close
	 */
	public long getDelayClose() {
		return this.delayClose;
	}

	/**
	 * Sets the delay close.
	 *
	 * @param delayClose the new delay close
	 */
	public void setDelayClose(long delayClose) {
		this.delayClose = delayClose;
	}

	/**
	 * Fixup display bounds.
	 *
	 * @param tipSize the tip size
	 * @param location the location
	 * @return the point
	 */
	private Point fixupDisplayBounds(Point tipSize, Point location) {
		if (this.respectDisplayBounds) {
			Rectangle bounds;
			final Point rightBounds = new Point(tipSize.x + location.x,
					tipSize.y + location.y);

			if (this.respectMonitorBounds) {
				bounds = this.shell.getDisplay().getPrimaryMonitor()
						.getBounds();
			} else {
				bounds = this.getPrimaryClientArea();
			}

			if (!(bounds.contains(location) && bounds.contains(rightBounds))) {
				if (rightBounds.x > bounds.x + bounds.width) {
					location.x -= rightBounds.x - (bounds.x + bounds.width);
				}

				if (rightBounds.y > bounds.y + bounds.height) {
					location.y -= rightBounds.y - (bounds.y + bounds.height);
				}

				if (location.x < bounds.x) {
					location.x = bounds.x;
				}

				if (location.y < bounds.y) {
					location.y = bounds.y;
				}
			}
		}

		return location;
	}

}
