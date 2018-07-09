package com.ljw.publish.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ljw.core.domain.RedisMessageRecord;

@RestController
@RequestMapping("/private/publishController")
public class PublishController {

	/**
	 * redis内部存储的数据标识
	 */
	private static final String KEY_STORE = "KeyStore";
	
	
	@Value("${redis.cache.store.signal}")
	private String topic;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	
	@Autowired
	private RedisTemplate<String, List<RedisMessageRecord>> redisTemplate;
	
	/**
	 * 发送消息
	 * 消息内容格式标准
	 * 
	 * operator_messageBody
	 * 
	 */
	@RequestMapping("/publish")
	public String sendMessage(@RequestParam (required=false)String message) {
		
		stringRedisTemplate.convertAndSend("topic1", "ljw");//发送消息
		stringRedisTemplate.convertAndSend("topic2", "lwb");//发送消息
		
		return "Send Message: topic1"+"topic2"+" Success!";
		
	}
	
	
	/**
	 * 获取记录信息
	 */
	@RequestMapping("/getRecodes")
	public List<RedisMessageRecord> getRecodes() {
		List<RedisMessageRecord> redisRecordList = redisTemplate.opsForValue().get(KEY_STORE);
		return redisRecordList;
	}
	
	
}
