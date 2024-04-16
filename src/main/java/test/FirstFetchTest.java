/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;


import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.pubmed.query.QueryEngine;
import org.topicquests.research.carrot2.redis.RedisClient;

/**
 * @author park
 *
 */
public class FirstFetchTest {
	private Environment environment;
	private RedisClient redis;
	private final String Topic; 

	private final String query = "Anthocyanins";

	/**
	 * 
	 */
	public FirstFetchTest() {
		environment = new Environment();
		Topic = environment.getStringProperty("REDIS_TOPIC");
		redis = environment.getRedis();
		QueryEngine qe = environment.getQueryEngine();
		qe.runQuery(query);
		
		//long i = 0;
		//while  (redis.getNext(Topic) != null)
	}

}
