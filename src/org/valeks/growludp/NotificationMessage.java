/**
 * This file is part of Personal Network Monitoring System.
 *
 * (C) 2010 Valentin Alexeev
 *
 * The software is licensed under Apache 2 License.
 */
package org.valeks.growludp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author valeks
 *
 */
class NotificationMessage extends Message {
	private final String notification;
	private final String title;
	private final String description;
	private final String appname;
	private final boolean isSticky;
	private final byte priority;
	
	/**
	 * Create new notification message.
	 * @param app reference to connection
	 * @param hasAuth do we require authenticated messages
	 */
	public NotificationMessage(Growl conn, boolean hasAuth,
			String _notification, String _title, String _description,
			String _appname, boolean _isSticky, byte _priority) {
		super(conn, hasAuth ? GrowlConstants.GROWL_TYPE_NOTIFICATION : GrowlConstants.GROWL_TYPE_NOTIFICATION_NOAUTH);
		notification = _notification;
		title = _title;
		description = _description;
		appname = _appname;
		isSticky = _isSticky;
		priority = _priority;
	}

	@Override
	protected void writePayload(ByteBuffer b) throws UnsupportedEncodingException {
		b.putShort((short) 0); // not sticky with default priority
		b.putShort((short) notification.length());
		b.putShort((short) title.length());
		b.putShort((short) description.length());
		b.putShort((short) appname.length());
		b.put(notification.getBytes("UTF-8"));
		b.put(title.getBytes("UTF-8"));
		b.put(description.getBytes("UTF-8"));
		b.put(appname.getBytes("UTF-8"));
	}
}
