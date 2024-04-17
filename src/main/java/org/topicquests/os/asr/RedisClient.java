/**
 * 
 */
package org.topicquests.os.asr;

import redis.clients.jedis.JedisPooled;

/**
 * @author jackpark
 * @see https://github.com/redis/jedis
 * @see https://redis.io/docs/latest/develop/connect/clients/java/jedis/
 * @see https://redis.io/docs/latest/commands/?group=list
 */
public class RedisClient {
	private Environment environment;
	private JedisPooled jedis;
	private final String JEDIS_BASE;
	private final int JEDIS_PORT = 6379;
	/**
	 * 
	 */
	public RedisClient(Environment env) {
		environment = env;
		JEDIS_BASE = environment.getStringProperty("RedisBase");
		jedis = new JedisPooled(JEDIS_BASE, JEDIS_PORT);
	}

	/**
	 * Add fresh {@code cargo} to {@code topic}
	 * @param topic
	 * @param cargo
	 * @return
	 */
	public long add(String topic, String cargo) {
		return jedis.lpush(topic, cargo);
	}
	
	/**
	 * Pop first topic off list
	 * @param topic
	 * @return can return {@code null}
	 */
	public String getNext(String topic) {
		return jedis.lpop(topic);
	}
}
