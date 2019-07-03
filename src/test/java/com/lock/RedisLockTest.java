package com.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.ContextHolder;
import com.lock.impl.RedisLock;

/**
 * 
 * {@link RedisLock}测试<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年7月1日<br>
 */
public class RedisLockTest {
	private static final Logger logger = LoggerFactory.getLogger(RedisLockTest.class);

	private RedisLock lock = ContextHolder.context.getBean(RedisLock.class);

	@SuppressWarnings("unchecked")
	private RedisTemplate<String, Object> redisTemplate = ContextHolder.context.getBean(RedisTemplate.class);

	@Test
	public void test1() throws Exception {
		int size = 100;
		final CountDownLatch slatch = new CountDownLatch(1);
		final CountDownLatch latch = new CountDownLatch(size);
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < size; i++) {
			exec.submit(new Runnable() {
				public void run() {
					try {
						slatch.await();
					} catch (InterruptedException e1) {
						logger.error("Exception", e1);
					}

					try {
						while (!lock.acquire()) {
						}
						logger.info("{}获取到锁", Thread.currentThread().getName());
					} catch (Exception e1) {
						logger.error("Exception", e1);
					} finally {
						try {
							lock.release();
							logger.info("{}已释放", Thread.currentThread().getName());
							latch.countDown();
						} catch (Exception e) {
							logger.error("Exception", e);
						}
					}
				}
			});
		}

		slatch.countDown();
		latch.await();
		exec.shutdown();
	}

	@Test
	public void test2() throws Exception {
		logger.info("{}", redisTemplate.opsForValue().get("a"));
	}
}
