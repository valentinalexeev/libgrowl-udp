/**
 * This file is part of Personal Network Monitoring System.
 *
 * (C) 2010 Valentin Alexeev
 *
 * The software is licensed under Apache 2 License.
 */
package org.valeks.growludp;

import java.io.IOException;

import org.junit.Test;

/**
 * @author valeks
 *
 */
public class TestGrowl {

	/**
	 * Test method for {@link org.valeks.growludp.Growl#register()}.
	 */
	//@Test
	public void testRegister() throws IOException {
		Growl g = new Growl("127.0.0.1", 9887, "cZpFrki");
		g.addNotification("Test notification", false);
		g.addNotification("Test notification 1", false);
		g.register();
	}

	/**
	 * Test method for {@link org.valeks.growludp.Growl#sendNotification(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	//@Test
	public void testSendNotification() throws IOException {
		Growl g = new Growl("127.0.0.1", 9887, "cZpFrki");
		g.addNotification("Test notification", false);
		g.addNotification("Test notification 1", false);
		g.register();
		
		g.sendNotification("Test notification", "Test", "This is a test notification.");
	}

	@Test
	public void testMimicGrowlNotify() throws IOException {
		Growl g = new Growl("127.0.0.1", 9887, "cZpFrki");
		g.setApplicationName("growlnotify");
		g.addNotification("Command-Line Growl Notification", true);
		g.register();
		
		g.sendNotification("Command-Line Growl Notification", "", "test test test");		
	}
}
