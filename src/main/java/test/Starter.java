/**
 * 
 */
package test;

import org.topicquests.research.carrot2.BatchQueryFileHandler;
import org.topicquests.research.carrot2.Environment;

/**
 * 
 */
public class Starter {
	private Environment environment;

	/**
	 * 
	 */
	public Starter() {
		environment = new Environment();
		BatchQueryFileHandler h = environment.getBatchFileHandler();
		h.runBatchQueries();
	}

	public static void main(String[] args) {
		new BatchTest();
	}
}
