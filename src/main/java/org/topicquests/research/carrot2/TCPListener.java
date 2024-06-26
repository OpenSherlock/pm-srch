/*
 * Copyright 2024 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2;

import java.net.ServerSocket;

import org.topicquests.os.asr.Environment;

/**
 * @author park
 *
 */
public class TCPListener {
	private Environment environment;
	private final String serverName = "localhost";
	private final int port = 8999;
	
	/**
	 * 
	 */
	public TCPListener(Environment env) {
		environment = env;
		new Worker().start();
	}

	class Worker extends Thread {
		
		public void run() {
			ServerSocket skt = null;
			try {
				skt = new ServerSocket(port);
				skt.accept();
				environment.shutDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
