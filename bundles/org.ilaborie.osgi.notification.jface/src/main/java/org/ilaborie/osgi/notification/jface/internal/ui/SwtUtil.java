package org.ilaborie.osgi.notification.jface.internal.ui;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

// TODO: Auto-generated Javadoc
/**
 * The Class SwtUtil.
 *
 * @author Mik Kersten
 * @author Steffen Pingel
 */
public class SwtUtil {

	/** The Constant FADE_RESCHEDULE_DELAY. */
	public static final long FADE_RESCHEDULE_DELAY = 80;

	/** The Constant FADE_IN_INCREMENT. */
	public static final int FADE_IN_INCREMENT = 15;

	/** The Constant FADE_OUT_INCREMENT. */
	public static final int FADE_OUT_INCREMENT = -20;

	/**
	 * Collect item data.
	 *
	 * @param items the items
	 * @param allVisible the all visible
	 */
	public static void collectItemData(TreeItem[] items, Set<Object> allVisible) {
		for (final TreeItem item : items) {
			allVisible.add(item.getData());
			collectItemData(item.getItems(), allVisible);
		}
	}

	/**
	 * Fast fade in.
	 *
	 * @param shell the shell
	 * @param listener the listener
	 * @return the fade job
	 */
	public static FadeJob fastFadeIn(Shell shell, IFadeListener listener) {
		return new FadeJob(shell, 2 * FADE_IN_INCREMENT, FADE_RESCHEDULE_DELAY,
				listener);
	}

	/**
	 * Fade in.
	 *
	 * @param shell the shell
	 * @param listener the listener
	 * @return the fade job
	 */
	public static FadeJob fadeIn(Shell shell, IFadeListener listener) {
		return new FadeJob(shell, FADE_IN_INCREMENT, FADE_RESCHEDULE_DELAY,
				listener);
	}

	/**
	 * Fade out.
	 *
	 * @param shell the shell
	 * @param listener the listener
	 * @return the fade job
	 */
	public static FadeJob fadeOut(Shell shell, IFadeListener listener) {
		return new FadeJob(shell, FADE_OUT_INCREMENT, FADE_RESCHEDULE_DELAY,
				listener);
	}

	/**
	 * The Class FadeJob.
	 */
	public static class FadeJob extends Job {

		/** The shell. */
		private final Shell shell;

		/** The increment. */
		private final int increment;

		/** The stopped. */
		private volatile boolean stopped;

		/** The current alpha. */
		private volatile int currentAlpha;

		/** The delay. */
		private final long delay;

		/** The fade listener. */
		private final IFadeListener fadeListener;

		/**
		 * Instantiates a new fade job.
		 *
		 * @param shell the shell
		 * @param increment the increment
		 * @param delay the delay
		 * @param fadeListener the fade listener
		 */
		public FadeJob(Shell shell, int increment, long delay,
				IFadeListener fadeListener) {
			super("Fading"); //$NON-NLS-1$
			if ((increment < -255) || (increment == 0) || (increment > 255)) {
				throw new IllegalArgumentException(
						"-255 <= increment <= 255 && increment != 0"); //$NON-NLS-1$
			}
			if (delay < 1) {
				throw new IllegalArgumentException("delay must be > 0"); //$NON-NLS-1$
			}
			this.currentAlpha = shell.getAlpha();
			this.shell = shell;
			this.increment = increment;
			this.delay = delay;
			this.fadeListener = fadeListener;

			this.setSystem(true);
			this.schedule(delay);
		}

		/**
		 * Canceling.
		 */
		@Override
		protected void canceling() {
			this.stopped = true;
		}

		/**
		 * Reschedule.
		 */
		private void reschedule() {
			if (this.stopped) {
				return;
			}
			this.schedule(this.delay);
		}

		/**
		 * Cancel and wait.
		 *
		 * @param setAlpha the set alpha
		 */
		public void cancelAndWait(final boolean setAlpha) {
			if (this.stopped) {
				return;
			}
			this.cancel();
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					if (setAlpha) {
						FadeJob.this.shell.setAlpha(FadeJob.this.getLastAlpha());
					}
				}
			});
		}

		/**
		 * Run.
		 *
		 * @param monitor the monitor
		 * @return the i status
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			if (this.stopped) {
				return Status.OK_STATUS;
			}

			this.currentAlpha += this.increment;
			if (this.currentAlpha <= 0) {
				this.currentAlpha = 0;
			} else if (this.currentAlpha >= 255) {
				this.currentAlpha = 255;
			}

			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					if (FadeJob.this.stopped) {
						return;
					}

					if (FadeJob.this.shell.isDisposed()) {
						FadeJob.this.stopped = true;
						return;
					}

					FadeJob.this.shell.setAlpha(FadeJob.this.currentAlpha);

					if (FadeJob.this.fadeListener != null) {
						FadeJob.this.fadeListener.faded(FadeJob.this.shell,
								FadeJob.this.currentAlpha);
					}
				}
			});

			if ((this.currentAlpha == 0) || (this.currentAlpha == 255)) {
				this.stopped = true;
			}

			this.reschedule();
			return Status.OK_STATUS;
		}

		/**
		 * Gets the last alpha.
		 *
		 * @return the last alpha
		 */
		private int getLastAlpha() {
			return (this.increment < 0) ? 0 : 255;
		}

	}

	/**
	 * The listener interface for receiving Fading events.
	 */
	public static interface IFadeListener {

		/**
		 * Faded.
		 *
		 * @param shell the shell
		 * @param alpha the alpha
		 */
		public void faded(Shell shell, int alpha);

	}

}
