/**
 * 
 */
package org.topicquests.os.asr.enigines;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.RedisClient;

/**
 * 
 */
public class DBpediaEngine extends AbstractRedisEngine {
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
		super(env);
		REDIS_IN_TOPIC = environment.getStringProperty("REDIS_DBPED");
		REDIS_OUT_TOPIC = environment.getStringProperty("REDIS_TOPIC");
	}
	public void processDoc(String json) {
		//TODO
	}

}
