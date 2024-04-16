/**
 * 
 */
package org.topicquests.os.asr.enigines;

import org.topicquests.os.asr.Environment;

/**
 * @author jackpark
 * We harvest over an eventstream from a UMLS file reader
 */
public class UMLSEngine {
	private Environment environment;
	private boolean isRunning = true;
	/**
	 * 
	 */
	public UMLSEngine(Environment env) {
		environment = env;

	}

}
