/**
 * 
 */
package org.topicquests.os.asr.enigines;

import org.topicquests.os.asr.Environment;

/**
 * @author jackpark
 * We harvest over an eventstream from a UMLS file reader
 */
public abstract class UMLSEngine extends AbstractRedisEngine {
	private final String REDIS_IN_TOPIC;

	/**
	 * 
	 */
	public UMLSEngine(Environment env) {
		super(env);
		REDIS_IN_TOPIC = environment.getStringProperty("REDIS_IN_UMLW");

	}
	
	public void processDoc(String json) {
		
	}
	
}
