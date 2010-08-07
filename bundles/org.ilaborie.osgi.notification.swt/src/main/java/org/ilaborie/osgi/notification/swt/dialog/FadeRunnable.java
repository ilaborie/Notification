/**
 * 
 */
package org.ilaborie.osgi.notification.swt.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class FadeRunnable.
 *
 * @author igor
 */
public class FadeRunnable implements Runnable {

	// Constant
	/** The Constant FADE_TIMER. */
	private static final int FADE_TIMER = 50;

	// Attributes
	/** The shell. */
	private final Shell shell;

	/** The stop. */
	private final int stop;

	/** The increment. */
	private final int incr;

	// Constructor
	/**
	 * Instantiates a new fade runnable.
	 *
	 * @param shell the shell
	 * @param stop the stop
	 * @param incr the increment
	 */
	public FadeRunnable(Shell shell, int stop, int incr) {
		super();
		if (shell == null) {
			throw new IllegalArgumentException(
					"The shell shouldn't being null !"); //$NON-NLS-1$
		}
		this.shell = shell;
		this.stop = stop;
		this.incr = incr;
	}

	// Methods
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (!this.shell.isDisposed()) {
			int current = this.shell.getAlpha();
			current += this.incr;
			if (this.incr > 0 && current > this.stop) {
				// End fade int
				this.shell.setAlpha(this.stop);
				this.endFade();
			} else if (this.incr < 0 && current < this.stop) {
				// end fade out
				this.shell.setAlpha(this.stop);
				this.endFade();
			} else {
				this.shell.setAlpha(current);
				Display.getDefault().timerExec(FADE_TIMER, this);
			}
		}
	}

	/**
	 * End fade .
	 */
	protected void endFade() {
		// Hook method to handle end of Fade
	}
}
