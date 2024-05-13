/*
 * Copyright 2024 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.enigines;


import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.JSONDocumentObject;
import org.topicquests.os.asr.RedisClient;
import org.topicquests.os.asr.api.IDocumentType;
import org.topicquests.os.asr.pmc.PubMedFullReportPullParser;
import org.topicquests.research.carrot2.Accountant;
import org.topicquests.support.api.IResult;

import com.google.gson.JsonParser;

import com.google.gson.JsonObject;

/**
 * 
 */
public class PMCEngine {
	private Environment environment;
	private Accountant accountant;
	private PubMedFullReportPullParser parser;
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
		parser = new PubMedFullReportPullParser(environment, null);
		worker = new Worker();
		worker.start();
	}
	
	class Worker extends Thread {
		
		public void run() {
			String json;
			while (isRunning) {
				json = redis.getNext(REDIS_IN_TOPIC);
				System.out.println("JSON "+json);
				while (json == null) {
					synchronized(waitObject) {
						try {
							waitObject.wait(WAIT_TIME);
						} catch(Exception e) {}
						if (!isRunning)
							return;
					}
					json = redis.getNext(REDIS_IN_TOPIC);
					System.out.println("JSOX "+json);
				}
				
				performMagic(json);
			}
		}
	}
	
	void performMagic(String json) {
		JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
		String xml = jo.get("cargo").getAsString();
		IResult r = parser.parseXML(xml);
		JSONDocumentObject j = (JSONDocumentObject)r.getResultObject();
		j.setDocType(IDocumentType.PMC);
		System.out.println("FOO\n"+j.getData().toString());
		if (true) return;
		String _pmcid = "pmc"+j.getPMCID();
		boolean sPMID = environment.getAccountant().seenBefore(j.getPMID());
		boolean sPMC = environment.getAccountant().seenBefore(_pmcid);
		if (sPMID && sPMC) return;
		environment.getAccountant().haveSeen(j.getPMID());
		environment.getAccountant().haveSeen(_pmcid);
		jo = new JsonObject();
		jo.addProperty("cargo", j.getData().toString());
		if (sPMID)
			jo.addProperty("verb", "update");
		redis.add(REDIS_OUT_TOPIC, jo.toString());

	}
	public void shutDown() {
		synchronized(waitObject) {
			isRunning = false;
			waitObject.notify();
		}		
	}

}
