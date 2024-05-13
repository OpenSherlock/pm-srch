/*
 * Copyright 2024 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.enigines;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.RedisClient;

/**
 * 
 */
public abstract class AbstractRedisEngine {
	protected Environment environment;
	private boolean isRunning = true;
	protected boolean hasBeenRunning = true;
	public RedisClient redis;
	private List<String> docs;
	private Worker worker;
	/**
	 * 
	 */
	public AbstractRedisEngine(Environment env) {
		environment = env;
		redis = environment.getRedis();
		docs = new ArrayList<String>();
		isRunning = true;
		worker = new Worker();
		worker.start();
	}
	
	
	abstract void processDoc(String json);
	
	public void addDoc(String xml) {
		environment.logDebug("PT.add");
		synchronized(docs) {
			docs.add(xml);
			docs.notify();
		}
	}

	
	class Worker extends Thread {
		public void run() {
			environment.logDebug("PubMedEngine.starting");
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
	}
	
	public void shutDown() {
		synchronized(docs) {
			isRunning = false;
			docs.notify();
		}
	}

}
