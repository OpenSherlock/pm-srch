/**
 * 
 */
package org.topicquests.os.asr.umls;

import org.topicquests.research.carrot2.Environment;

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
