/**
 * 
 */
package org.topicquests.os.asr.enigines;

import java.util.List;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.RedisClient;
import org.topicquests.os.asr.enigines.AbstractRedisEngine.Worker;
import org.topicquests.research.carrot2.Accountant;

/**
 * 
 */
public class PMCEngine {
	private Environment environment;
	private Accountant accountant;
	private boolean isRunning = true;
	protected boolean hasBeenRunning = true;
	public RedisClient redis;
	private Object waitObject = new Object();
	private final long WAIT_TIME = 30000; // 30 seconds 

	private Worker worker;
	private final String REDIS_IN_TOPIC, REDIS_OUT_TOPIC;
	/**
	 * 
	 */
	public PMCEngine(Environment env) {
		environment = env;
		redis = environment.getRedis();
		accountant = environment.getAccountant();
		REDIS_IN_TOPIC = environment.getStringProperty("REDIS_IN_PMC");
		REDIS_OUT_TOPIC = environment.getStringProperty("REDIS_TOPIC");
		worker = new Worker();
		worker.start();
	}
	
	class Worker extends Thread {
		
		public void run() {
			String json;
			while (isRunning) {
				json = redis.getNext(REDIS_IN_TOPIC);
				while (json == null) {
					synchronized(waitObject) {
						try {
							waitObject.wait(WAIT_TIME);
						} catch(Exception e) {}
						if (!isRunning)
							return;
					}
					json = redis.getNext(REDIS_IN_TOPIC);
				}
				
				performMagic(json);
			}
		}
	}
	
	void performMagic(String json) {
	
	}
	public void shutDown() {
		synchronized(waitObject) {
			isRunning = false;
			waitObject.notify();
		}		
	}

}
