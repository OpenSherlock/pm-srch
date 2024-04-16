/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

import org.topicquests.os.asr.Environment;
import org.topicquests.research.carrot2.pubmed.query.BatchQueryFileHandler;

/**
 * @author park
 *
 */
public class BatchTest {
	private Environment environment;

	/**
	 * 
	 */
	public BatchTest() {
		environment = new Environment();
		BatchQueryFileHandler h = environment.getBatchFileHandler();
		//h.runBatchQueries();
		h.runSimpleBatchQueries();
	}

	public static void main(String[] args) {
		new BatchTest();
	}

}
