/**
 * 
 */
package org.ilaborie.osgi.notification.swt.dialog;

import org.eclipse.swt.widgets.Shell;

/**
 * The Class ShellCloser.
 *
 * @author igor
 */
public class ShellCloser implements Runnable {

	/** The shell. */
	private final Shell shell;

	/**
	 * Instantiates a new shell closer.
	 *
	 * @param shell the shell
	 */
	public ShellCloser(Shell shell) {
		super();
		if (shell == null) {
			throw new IllegalArgumentException("Shell shouldn't being empty !"); //$NON-NLS-1$
		}
		this.shell = shell;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (!this.shell.isDisposed()) {
			this.shell.close();
		}
	}

}
