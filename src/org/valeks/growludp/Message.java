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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A base class for registration and notification messages.
 * 
 * @author (C) 2010 Valentin Alexeev
 */
abstract class Message {
	private final byte version = GrowlConstants.GROWL_PROTOCOL_VERSION;
	private final byte type;
	private final Growl conn;
	
	/**
	 * Create new message.
	 * @param _conn connection this message is attached to.
	 * @param _type type of message to create.
	 */
	Message(Growl _conn, byte _type) {
		type = _type;
		conn = _conn;
	}
	
	/**
	 * Implementation-specific payload encoding.
	 * @param b buffer where to write the encoded payload.
	 */
	protected abstract void writePayload(ByteBuffer b) throws Exception;
	
	/**
	 * Retrieve byte array presentation of the message.
	 * 
	 * @throws IllegalArgumentException if there were errors encoding message (see exception's cause).
	 * @return fully complete message to send to Growl.
	 */
	public byte[] getData() throws IllegalArgumentException {
		ByteBuffer result = ByteBuffer.allocate(2000);

		// add common header
		result.put(version);
		result.put(type);
		
		// ask implementation to put in payload
		try {
			writePayload(result);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to encode payload: " + e.toString(), e);
		}
		
		// mark position for checksum insertion.
		int pos = result.position();
		// only checksum when asked.
		if (type != GrowlConstants.GROWL_TYPE_NOTIFICATION_NOAUTH
				&& type != GrowlConstants.GROWL_TYPE_REGISTRATION_NOAUTH) {
			// go to the beginning
			byte[] md5;
			try {

				// get the data for MD5
				byte[] data = new byte[result.position()];

				result.rewind();
				result.get(data, 0, data.length);

				md5 = md5(data, conn.getPassword().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Unable to encode password: " + e.toString(), e);
			}
			// return to checksum position
			result.position(pos);
			// do MD5 and insert 16 bytes into the message
			result.put(md5);
		}

		byte[] res = new byte[result.position()];

		result.rewind();
		result.get(res, 0, res.length);

		return res;
	}
	
	/**
	 * Generate MD5 checksum for input message.
	 * @param input MD5 input
	 */
	static byte[] md5(byte[] data, byte[] pass) {
	    MessageDigest md5;
	    try {
	      md5 = MessageDigest.getInstance("MD5");
	      md5.reset();
	      md5.update(data);
	      md5.update(pass);
	      return md5.digest();
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
		}
	    return null;
	}
}
