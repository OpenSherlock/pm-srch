/**
 * 
 */
package org.topicquests.os.asr.pmc;

import java.io.*;
import java.util.*;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.file.FileHandler;

/**
 * @author park
 *
 */
public class FullDocumentReader {
	private Environment environment;
	private PubMedFullReportPullParser parser;
	private List<String>documentPaths;
	private boolean isRunning = true;
	private StringBuilder buf;
	private Worker thread;
	/**
	 * 
	 */
	public FullDocumentReader(Environment env) {
		environment = env;
		
		documentPaths = new ArrayList<String>();
		buf = new StringBuilder();
		thread = new Worker();
		thread.start();
	}

	public void setHandler(FileHandler h) {
		parser = new PubMedFullReportPullParser(environment, h);
	}
	public void shutDown() {
		synchronized(documentPaths) {
			isRunning = false;
			documentPaths.notify();
			System.out.println("FullDocumentReader.shutDown");
		}
	}
	
	public void addPath(String path) {
		synchronized(documentPaths) {
			documentPaths.add(path);
			documentPaths.notify();
		}
	}
	
	class Worker extends Thread {
		
		public Worker() {
			//TODO
		}
		
		public void run() {
			String path = null;
			while (isRunning) {
				synchronized(documentPaths) {
					if (documentPaths.isEmpty()) {
						try {
							documentPaths.wait();
						} catch (Exception e) {}
					} else {
						path = documentPaths.remove(0);
					}
				}
				if (isRunning && path != null) {
					processPath(path);
					path = null;
				}
			}
		}
		
		void processPath(String path) {
			System.out.println(path);
			File f = new File(path);
			if (f.exists()) {
				parser.parseXML(f);
			} else {
				System.out.println("MISSING FILE");
			}
		}
	}
}
