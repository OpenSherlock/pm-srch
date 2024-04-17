/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.enigines;

import java.util.*;

import org.topicquests.os.asr.Environment;
import org.topicquests.os.asr.JSONDocumentObject;
import org.topicquests.os.asr.RedisClient;
import org.topicquests.research.carrot2.pubmed.PubMedReportPullParser;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class PubMedEngine extends AbstractRedisEngine {
	private PubMedReportPullParser parser;
	private final String REDIS_OUT_TOPIC;

	/**
	 * 
	 */
	public PubMedEngine(Environment env) {
		super( env);
		REDIS_OUT_TOPIC = environment.getStringProperty("REDIS_TOPIC");

		parser = new PubMedReportPullParser(environment);

		environment.logDebug("PubMedEngine");
	}
	

	
	/**
	 * This receives {@code xml} and will get back
	 * an object which must then be sent off to 
	 * another thread for turning into an IDocument
	 * @param xml
	 */
	void processDoc(String xml) {
		environment.logDebug("PT.process "+parser);
		hasBeenRunning = true;
		IResult r = parser.parseXML(xml);
		JSONDocumentObject j = (JSONDocumentObject)r.getResultObject();
		//environment.logDebug("PT+");
		environment.getAccountant().haveSeen(j.getPMID());
		redis.add(REDIS_OUT_TOPIC, j.toString());
	}
}

