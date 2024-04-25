/**
 * 
 */
//package org.topicquests.harvester.pubmed;
package org.topicquests.os.asr.file;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.topicquests.os.asr.Environment;
/**
 * @author park
 *
 */
public class FileHandler {
	private Environment environment;

	/**
	 * @param enc
	 */
	public FileHandler(Environment env) {
		environment = env;
	}

	
	/**
	 * Recursively walk down a given <code>directory</code> sending
	 * file paths to the reader.
	 * @param directory
	 */
	public void processDirectory(File directory) {
		File [] files = directory.listFiles();
		File f;
		int len = files.length;
		for (int i=0;i<len;i++) {
			f = files[i];
			if (f.isDirectory())
				processDirectory(f); // recurse
			else
				processFile(f);
		}
	}
	
	void processFile(File xml) {
		//TODO
	}
}
