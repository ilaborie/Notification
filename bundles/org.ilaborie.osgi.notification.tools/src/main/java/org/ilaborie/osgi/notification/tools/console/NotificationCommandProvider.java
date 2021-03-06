/**
 * 
 */
package org.ilaborie.osgi.notification.tools.console;

import java.text.MessageFormat;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.ilaborie.osgi.notification.INotification;
import org.ilaborie.osgi.notification.INotificationService;
import org.ilaborie.osgi.notification.impl.NotificationImpl;

/**
 * @author igor
 *
 */
public class NotificationCommandProvider implements CommandProvider {

	/** The notification service. */
	private INotificationService notificationService;

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		this.addHeader("OSGi notification commands", sb); //$NON-NLS-1$
		this.addCommand("notify <message> [<title>]", "notify the message", sb); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	/**
	 * The notify command.
	 *
	 * @param intp the interpreter
	 * @return <code>null</code>
	 */
	public Object _notify(CommandInterpreter intp) {
		String arg0 = intp.nextArgument();
		if (this.notificationService == null) {
			intp.println("No notification service available !"); //$NON-NLS-1$
		} else if (arg0 == null || "".equals(arg0)) { //$NON-NLS-1$
			intp.println("No message specified !"); //$NON-NLS-1$
		} else {
			assert arg0 != null;
			String arg1 = intp.nextArgument();
			INotification notification;
			if (arg1 == null || "".equals(arg1)) { //$NON-NLS-1$
				notification = new NotificationImpl(arg0);
			} else {
				notification = new NotificationImpl(arg0, arg1);
			}
			this.notificationService.show(notification);
		}
		return null;
	}

	/**
	 * Add a command.
	 * @param cmdLabel the command label.
	 * @param cmdDesc the command description.
	 * @param sb the builder.
	 */
	private void addCommand(String cmdLabel, String cmdDesc, StringBuilder sb) {
		sb.append(MessageFormat.format("\t{0} - {1}\n", cmdLabel, cmdDesc)); //$NON-NLS-1$
	}

	/**
	 * Add a Header.
	 * @param label the header label.
	 * @param sb the builder.
	 */
	private void addHeader(String label, StringBuilder sb) {
		sb.append(MessageFormat.format("--- {0} ---\n", label)); //$NON-NLS-1$
	}

	/**
	 * Sets the notification service.
	 *
	 * @param notificationService the notificationService to set
	 */
	public void setNotificationService(INotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * Sets the notification service.
	 *
	 * @param oldNotificationService the notificationService to set
	 */
	public void unsetNotificationService(
			INotificationService oldNotificationService) {
		if (this.notificationService != null
				&& this.notificationService.equals(oldNotificationService)) {
			this.notificationService = oldNotificationService;
		}
	}

}
