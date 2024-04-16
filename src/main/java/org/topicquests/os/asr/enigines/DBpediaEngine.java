/**
 * 
 */
package org.topicquests.os.asr.enigines;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.os.asr.Environment;
import org.topicquests.research.carrot2.redis.RedisClient;

/**
 * 
 */
public class DBpediaEngine {
	private Environment environment;
	private boolean isRunning = true;
	private boolean hasBeenRunning = true;
	private Worker worker;
	private RedisClient redis;
	private List<String> docs;

	private final String REDIS_IN_TOPIC;
	private final String REDIS_OUT_TOPIC;

	//////////////////////////
	// Like pubmed, this needs an accountant which records
	// all dbpedia hits so we don't duplicate them.
	/////////////////////////
	/**
	 * 
	 */
	public DBpediaEngine(Environment env) {
		environment = env;
		REDIS_IN_TOPIC = environment.getStringProperty("REDIS_DBPED");
		REDIS_OUT_TOPIC = environment.getStringProperty("REDIS_TOPIC");
		redis = environment.getRedis();
		docs = new ArrayList<String>();
		isRunning = true;
		worker = new Worker();
		worker.start();
	}

	class Worker extends Thread {
		public void run() {
			environment.logDebug("ParserThread.starting");
			String doc = null;
			while (isRunning) {
				synchronized(docs) {
					if (docs.isEmpty()) {
						if (hasBeenRunning) {
							environment.queueEmpty();
							hasBeenRunning  = false;
						}
						try {
							docs.wait();
						} catch (Exception e) {}
						
					} else {
						doc = docs.remove(0);
					}
				}
				if (isRunning && doc != null) {
					processDoc(doc);
					doc = null;
				}
			}
		}
		
		void processDoc(String json) {
			//TODO
		}

	}
	
	public void shutDown() {
		synchronized(docs) {
			isRunning = false;
			docs.notify();
		}
	}

}
