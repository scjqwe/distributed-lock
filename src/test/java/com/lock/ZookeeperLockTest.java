package com.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
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
						lock.acquire();
						logger.info("{}获取到锁", Thread.currentThread().getName());
						Thread.sleep(1000);
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
		CuratorFramework client = lock.getClient();
		client.start();
		client.createContainers("/lock");
		client.close();
	}

}
