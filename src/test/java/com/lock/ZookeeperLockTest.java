package com.lock;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ContextHolder;
import com.lock.impl.ZookeeperLock;

/**
 * 
 * {@link ZookeeperLock}测试<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public class ZookeeperLockTest {
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperLockTest.class);

	private ZookeeperLock lock = ContextHolder.context.getBean(ZookeeperLock.class);

	@Test
	public void test1() throws Exception {
		final CountDownLatch slatch = new CountDownLatch(1);
		final CountDownLatch latch = new CountDownLatch(5);
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			exec.submit(new Runnable() {
				public void run() {
					try {
						slatch.await();
					} catch (InterruptedException e1) {
						logger.error("Exception", e1);
					}

					CuratorFramework client = lock.getClient();
					client.start();
					InterProcessMutex mutex = new InterProcessMutex(client, "/lock");

					try {
						mutex.acquire();
					} catch (Exception e1) {
						logger.error("Exception", e1);
					}

					System.out.println(Thread.currentThread().getName() + "获取到锁");
					try {
						latch.countDown();
						System.out.println(Thread.currentThread().getName() + "已释放");
					} catch (Exception e) {
						logger.error("Exception", e);
					} finally {
						try {
							mutex.release();
							client.close();
						} catch (Exception e) {
							logger.error("Exception", e);
						}
					}
				}
			});
		}

		slatch.countDown();
		try {
			latch.await();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		exec.shutdown();
	}

	@Test
	public void test2() throws Exception {
		CuratorFramework client = lock.getClient();
		client.start();
		client.createContainers("/lock");
		client.close();
	}

}
