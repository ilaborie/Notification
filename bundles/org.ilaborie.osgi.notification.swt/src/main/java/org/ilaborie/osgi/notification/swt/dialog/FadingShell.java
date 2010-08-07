package org.ilaborie.osgi.notification.swt.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class FadingShell.
 *
 * @author igor
 */
public class FadingShell {
	// Constants
	/** The Constant TRANSPARENT. */
	private static final int TRANSPARENT = 0x00;

	/** The Constant FADE_IN_STEP. */
	private static final int FADE_IN_STEP = 32;

	// how many tick steps we use when fading out 
	/** The Constant FADE_OUT_STEP. */
	private static final int FADE_OUT_STEP = 16;

	// Attributes
	/** The shell. */
	private final Shell shell;

	/** The initial alpha. */
	private final int targetAlpha;

	/** The shell closer. */
	private final ShellCloser shellCloser;

	/** The timeout. */
	private final int timeout;

	// Constructor
	/**
	 * Instantiates a new fading shell listener.
	 *
	 * @param shell the shell
	 * @param targetAlpha the target alpha
	 * @param timeout the timeout
	 */
	public FadingShell(final Shell shell, int targetAlpha, int timeout) {
		super();
		if (shell == null) {
			throw new IllegalArgumentException(
					"The shell shouldn't being null !"); //$NON-NLS-1$
		}
		this.shell = shell;
		this.targetAlpha = targetAlpha;
		this.shellCloser = new ShellCloser(this.shell) {
			/* (non-Javadoc)
			 * @see org.ilaborie.osgi.notification.swt.dialog.ShellCloser#closeShell()
			 */
			@Override
			protected void closeShell() {
				FadeRunnable fade = new FadeRunnable(shell, TRANSPARENT,
						-FADE_OUT_STEP) {
					/* (non-Javadoc)
					 * @see org.ilaborie.osgi.notification.swt.dialog.FadeRunnable#endFade()
					 */
					@Override
					protected void endFade() {
						shell.close();
					}
				};
				fade.run();
			}
		};
		this.timeout = timeout;
	}

	// Methods

	/**
	 * Fade in.
	 */
	public void fadeIn() {
		final int fadeOutTimeout = FadingShell.this.timeout;
		final ShellCloser shellFadeOutCloser = FadingShell.this.shellCloser;
		// Fade in
		FadeRunnable fade = new FadeRunnable(this.shell, this.targetAlpha,
				FADE_IN_STEP) {
			/* (non-Javadoc)
			 * @see org.ilaborie.osgi.notification.swt.dialog.FadeRunnable#endFade()
			 */
			@Override
			protected void endFade() {
				super.endFade();
				if (fadeOutTimeout > 0) {
					Display.getCurrent().timerExec(fadeOutTimeout,
							shellFadeOutCloser);
				}
			}
		};
		fade.run();
	}

	/**
	 * Fade out.
	 */
	public void fadeOut() {
		// Fade out 
		FadeRunnable fade = new FadeRunnable(this.shell, TRANSPARENT,
				-FADE_OUT_STEP);
		fade.run();
	}

	// Getters
	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public Shell getShell() {
		return this.shell;
	}
}