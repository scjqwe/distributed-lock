package com.lock.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
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

	/** 默认锁键 */
	private String lKey = "lock";

	/** 默认锁值 */
	private String lValue = "1";

	public boolean acquire() {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] serializeKey = redisTemplate.getStringSerializer().serialize(lKey);
				byte[] serializeValue = redisTemplate.getStringSerializer().serialize(String.valueOf(lValue));
				return connection.setNX(serializeKey, serializeValue);
			}
		});
	}

	public boolean acquire(long time, TimeUnit unit) {
		final long exp = unit.toSeconds(time);
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] serializeKey = redisTemplate.getStringSerializer().serialize(lKey);
				byte[] serializeValue = redisTemplate.getStringSerializer().serialize(String.valueOf(lValue));
				Boolean acquire = connection.setNX(serializeKey, serializeValue);
				// 如果设值成功,则设置过期时间
				if (acquire) {
					connection.expire(serializeKey, exp);
				}
				return acquire;
			}
		});
	}

	public boolean release() {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] serializeKey = redisTemplate.getStringSerializer().serialize(lKey);
				if (connection.exists(serializeKey)) {
					connection.del(serializeKey);
					return true;
				}
				return false;
			}
		});
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
		this.lKey = lKey;
	}

	public String getlValue() {
		return lValue;
	}

	public void setlValue(String lValue) {
		this.lValue = lValue;
	}

}
