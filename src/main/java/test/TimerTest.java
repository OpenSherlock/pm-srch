/**
 * 
 */
package test;

import org.topicquests.research.carrot2.Environment;

/**
 * @author park
 *
 */
public class TimerTest {
	private Environment environment;

	/**
	 * 
	 */
	public TimerTest() {
		environment = new Environment();
		environment.armTimer();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TimerTest();
	}

}
