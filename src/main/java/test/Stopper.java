/**
 * 
 */
package test;

import java.net.Socket;

import org.topicquests.os.asr.Environment;

/**
 * @author park
 * Stops a harvest process: allows file queue to flush
 */
public class Stopper {
	private static final String serverName = "localhost";
	private static final int port = 8999;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Stopping");
		try {
			Socket s = new Socket(serverName, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
