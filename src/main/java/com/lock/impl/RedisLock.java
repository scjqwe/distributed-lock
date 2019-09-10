package com.lock.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.lock.Lock;

/**
 * 
 * Redis分布式锁实现<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public class RedisLock implements Lock {

	private RedisTemplate<String, String> redisTemplate;

	private static final String PREFIX = "redis-lock:";

	/** 默认锁键 */
	private String lKey = PREFIX + UUID.randomUUID().toString();

	/** 默认锁值 */
	private String lValue = "1";

	/**
	 * 获取锁
	 */
	public boolean acquire() {
		return redisTemplate.opsForValue().setIfAbsent(lKey, lValue);
	}

	/**
	 * 获取锁
	 * 
	 * @param time
	 *            持有时间
	 * @param unit
	 *            时间单位
	 */
	public boolean acquire(long time, TimeUnit unit) {
		return redisTemplate.opsForValue().setIfAbsent(lKey, lValue, time, unit);
	}

	/**
	 * 释放锁
	 */
	public boolean release() {
		return redisTemplate.delete(lKey);
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String getlKey() {
		return lKey;
	}

	public void setlKey(String lKey) {
		this.lKey = PREFIX + lKey;
	}

	public String getlValue() {
		return lValue;
	}

	public void setlValue(String lValue) {
		this.lValue = lValue;
	}

}
