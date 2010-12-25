/**
 * This file is part of Personal Network Monitoring System.
 *
 * (C) 2010 Valentin Alexeev
 *
 * The software is licensed under Apache 2 License.
 */
package org.valeks.growludp;

/**
 * Various constants used in the protocol.
 * 
 * @author (C) 2010 Valentin Alexeev
 */
interface GrowlConstants {
	// protocol versions.
	/** Basic protocol version. */
	byte GROWL_PROTOCOL_VERSION = 1;
	/** 
	 * AES128 encrypted protocol version [not supported by libgrowl-udp].
	 * The value GROWL_PROTOCOL_VERSION_AES128 indicates that the rest of the packet is encrypted 
	 * using a 128-bit AES key. The AES key is derived from the password (PKCS12-style) and is used
	 * in Cipher Block Chaining mode with a fixed initialization vector and PKCS7 padding.
	 */
	byte GROWL_PROTOCOL_VERSION_AES128 = 2;
	
	// message types
	/** The packet type of registration packets with MD5 authentication. */
	byte GROWL_TYPE_REGISTRATION = 0;
	/** The packet type of notification packets with MD5 authentication. */
	byte GROWL_TYPE_NOTIFICATION = 1;
	/** The packet type of registration packets with SHA-256 authentication. */
	byte GROWL_TYPE_REGISTRATION_SHA256 = 2;
	/** The packet type of notification packets with SHA-256 authentication. */
	byte GROWL_TYPE_NOTIFICATION_SHA256 = 3;
	/** The packet type of registration packets without authentication. */
	byte GROWL_TYPE_REGISTRATION_NOAUTH = 4;
	/** The packet type of notification packets without authentication. */
	byte GROWL_TYPE_NOTIFICATION_NOAUTH = 5;
}
