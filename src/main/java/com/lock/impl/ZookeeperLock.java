package com.lock.impl;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lock.Lock;

/**
 * 
 * Zookeeper分布式锁实现<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public class ZookeeperLock implements Lock {
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

	/** Zookeeper客户端 */
	private CuratorFramework client;

	/** Zookeeper下指定的锁目录 */
	private String lockDir = "/curator/lock";

	/** 锁对象 */
	private InterProcessMutex mutex;

	/**
	 * 获取锁
	 */
	public boolean acquire() {
		try {
			if (client == null) {
				throw new RuntimeException("client can not be null");
			}
			if (mutex == null) {
				client.start();// 开启会话
				mutex = new InterProcessMutex(client, lockDir);
			}

			// 获取锁
			mutex.acquire();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		return false;
	}

	/**
	 * 获取锁
	 */
	public boolean acquire(long time, TimeUnit unit) {
		try {
			if (client == null) {
				throw new RuntimeException("client can not be null");
			}
			if (mutex == null) {
				client.start();// 开启会话
				mutex = new InterProcessMutex(client, lockDir);
			}

			// 获取锁
			return mutex.acquire(time, unit);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return false;
	}

	/**
	 * 释放锁
	 */
	public boolean release() {
		try {
			if (client == null) {
				throw new RuntimeException("client can not be null");
			}
			if (mutex == null) {
				throw new RuntimeException("mutex can not be null");
			}
			if (client.getState() != CuratorFrameworkState.STARTED) {
				throw new RuntimeException("client can not be stopped");
			}

			mutex.release();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (client != null) {// 关闭会话
				client.close();
			}
		}

		return false;
	}

	public CuratorFramework getClient() {
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	public String getLockDir() {
		return lockDir;
	}

	public void setLockDir(String lockDir) {
		this.lockDir = lockDir;
	}

}
