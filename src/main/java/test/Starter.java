/**
 * 
 */
package test;

import org.topicquests.os.asr.Environment;
import org.topicquests.research.carrot2.pubmed.query.BatchQueryFileHandler;

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
