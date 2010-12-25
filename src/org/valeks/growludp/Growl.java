/**
 * This file is part of Personal Network Monitoring System.
 *
 * (C) 2010 Valentin Alexeev
 *
 * The software is licensed under Apache 2 License.
 */
package org.valeks.growludp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A core class to send notifications.
 * @author (C) 2010 Valentin Alexeev
 */
public class Growl {
	private final String host;
	private final int port;
	private final String password;
	private String appName = "libgrowl-udp";
	private boolean hasRegistered = false;
	private final Map<String, Boolean> notifications = new LinkedHashMap<String, Boolean>();
	private DatagramSocket socket;
	
	public Growl(String _host, int _port, String _password) {
		host = _host;
		port = _port;
		password = _password;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// should not happen but what if?
			e.printStackTrace();
		}
	}
	
	public Growl(String _host, int _port) {
		this(_host, _port, null);
	}
	
	public Growl(String _host) {
		this(_host, 9887);
	}
	
	public Growl() {
		this("localhost");
	}
	
	String getPassword() {
		return password;
	}
	
	public void setApplicationName(String _appName) {
		appName = _appName;
	}
	
	public void addNotification(String notification, boolean isDefaultEnabled) {
		if (hasRegistered) {
			notifications.clear();
			hasRegistered = false;
		}
		notifications.put(notification, isDefaultEnabled);
	}
	
	public void register() throws IOException {
		if (hasRegistered) {
			return;
		}
		RegistrationMessage msg = new RegistrationMessage(this, password != null, appName);
		for (Map.Entry<String, Boolean> entry: notifications.entrySet()) {
			msg.addNotification(entry.getKey(), entry.getValue());
		}
		sendPacket(msg);
	}
	
	public void sendNotification(String notification, String title, String description) throws IOException {
		NotificationMessage msg = new NotificationMessage(this, password != null, notification, title, description, appName, false, (byte) 0);
		sendPacket(msg);
	}
	
	void sendPacket(Message msg) throws IOException {
		byte[] data = msg.getData();
		DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
		socket.send(packet);
	}
}
