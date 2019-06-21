package com.lock;

import java.util.concurrent.TimeUnit;

/**
 * 
 * 分布式锁接口<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public interface Lock {

	/**
	 * 获取锁
	 * 
	 * @return
	 */
	public boolean acquire();

	/**
	 * 获取锁
	 * 
	 * @param time
	 * @param unit
	 * @return
	 */
	public boolean acquire(long time, TimeUnit unit);

	/**
	 * 释放锁
	 * 
	 * @return
	 */
	public boolean release();

}
