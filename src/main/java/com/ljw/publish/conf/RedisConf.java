package com.ljw.publish.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis工作模式配置
 * @author ljw
 *
 */

@Configuration
@PropertySource("classpath:redis-conf.properties")
public class RedisConf {

	enum MODE {
		stand_alone, cluster, sentinel
	}
	@Value("${redis.cluster.nodes}")
	private List<String> clusterNodes;
	@Value("${redis.mode}")
	private String mode;

	@Value("${redis.sentinel.nodes}")
	private List<String> sentinelNodes;

	@Value("${redis.sentinel.master}")
	private String master4Sentinel;

	@Value("${redis.max-total}")
	private int maxTotal;

	@Value("${redis.max-idle}")
	private int maxIdle;

	@Value("${redis.stand_alone.host-name}")
	private String hostName;

	@Value("${redis.stand_alone.port}")
	private int port;

	@Value("${redis.db_idx}")
	private int database;
	
	@Value("${redis.cache_manager.default-expire-sec}")
	private int defaultExpireSec4CacheManager;

	@Value("${redis.cache.store.signal}")
	private String signal;
	
	@Bean
	public RedisConnectionFactory connectionFactory() throws Exception {

		MODE _mode = getMode();

		
		switch (_mode) {
		case stand_alone:
			return buildFactoryIsStandAlone();
			
		case cluster:
			return buildFactoryIsCluster();
			
		case sentinel:
			return buildFactoryIsSentinel();

		default:
			throw new Exception("RedisConnectionFactory build exception");
		}

	}
	
/**
 * ==================================================================================================================
 * 
 */
 
	/**
	 * 创建redis数据库接口，RedisTemplate<String,?>的bean对象就是数据库的接口，
	 * （StringRedisTemplate是相当于存储key为String, value也为String的特殊的接口）
	 * 
	 * 
	 * 可以用RedisTemplate的对象存储有String为key的各种对象
	 * 
	 * 
	 * 
	 * 此项目中使用了
	 * （1）RedisTemplate对象接口的查询数据方式，
	 * （2）StringRedisTemplate对象接口的消息发布模式
	 * 
	 * 
	 */

	/**
	 * RedisTemplate配置bean
	 */
	@Bean
	public 	RedisTemplate<String,?> template(RedisConnectionFactory connectionFactory){
		RedisTemplate<String,?> template =new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		
		return template;
	}
	
	/**
	 * StringRedisTemplate配置bean
	 */
	@Bean
	public StringRedisTemplate strTemplate(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
	
/**
 * =================================================================================================================
 * /
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//*************************************自己用的*****************************************
	
	
	
	/**
	 * 获取mode
	 */
	private MODE getMode() {

		return MODE.valueOf(mode);
	}
	

	/**
	 * stand_alone模式
	 * @return
	 */
	private RedisConnectionFactory buildFactoryIsStandAlone() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		
		JedisConnectionFactory factory = new JedisConnectionFactory(config);
		factory.setHostName("127.0.0.1");
		factory.setPort(port);
		factory.setUsePool(true);
		factory.setDatabase(database);
		factory.afterPropertiesSet();
		
		return factory;
	}
	
	/**
	 * Cluster集群模式
	 * @return
	 */
	private RedisConnectionFactory buildFactoryIsCluster() {
		JedisPoolConfig config =new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		
		RedisClusterConfiguration clusterConf = new RedisClusterConfiguration(clusterNodes);
		JedisConnectionFactory factory = new JedisConnectionFactory(clusterConf); 
		factory.setUsePool(true);
		factory.setDatabase(database);
		factory.setPoolConfig(config);
		
		return factory;
	}
	
	/**
	 * Sentinel监听模式
	 * @return
	 */
	private RedisConnectionFactory buildFactoryIsSentinel() {
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(master4Sentinel);
		String host;
		int port;
		for(String sentinelNode:sentinelNodes){
			host = sentinelNode.split(":")[0];
			port = Integer.parseInt(sentinelNode.split(":")[1]);
			sentinelConfig.addSentinel(new RedisNode(host, port));
		}
		
		JedisPoolConfig config =new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		
		JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfig);
		factory.setPoolConfig(config);
		factory.setUsePool(true);
		factory.setDatabase(database);
		
		return factory;
	}

	

}
