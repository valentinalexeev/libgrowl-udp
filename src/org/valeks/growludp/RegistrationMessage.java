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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author valeks
 *
 */
class RegistrationMessage extends Message {
	private final Map<String, Boolean> notifications = new LinkedHashMap<String, Boolean>();
	private final String appname;
	/** Number of 'default on' notifications. */ 
	private byte ndef;
	
	/**
	 * Create new registration packet.
	 * @param hasAuth include authentication into the packet
	 */
	RegistrationMessage(Growl conn, boolean hasAuth, String _appName) {
		super(conn, hasAuth ? GrowlConstants.GROWL_TYPE_REGISTRATION : GrowlConstants.GROWL_TYPE_REGISTRATION_NOAUTH);
		appname = _appName;
	}
	
	void addNotification(String _notification, boolean defaultEnabled) {
		notifications.put(_notification, defaultEnabled);
		if (defaultEnabled) {
			ndef++;
		}
	}
	
	@Override
	protected void writePayload(ByteBuffer b) throws UnsupportedEncodingException {
		// appname length
		b.putShort((short) appname.length());
		// nall
		b.put((byte) notifications.size());
		// ndef
		b.put(ndef);
		
		b.put(appname.getBytes("UTF-8"));
		// list of notifications
		for (String notification: notifications.keySet()) {
			b.putShort((short) notification.length());
			b.put(notification.getBytes("UTF-8"));
		}
		
		// put in ndefs
		byte index = 0;
		for (Map.Entry<String, Boolean> entry: notifications.entrySet()) {
			if (entry.getValue()) {
				b.put((byte) index);
			}
			index++;
		}
	}
}
