package com.lock.impl;

import java.util.concurrent.TimeUnit;

import com.lock.Lock;

/**
 * 
 * MySql分布式锁实现,不建议使用<br>
 * 参考:https://www.jianshu.com/p/b76f409b2db2<br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public class MySqlLock implements Lock {

	public boolean acquire() {
		return false;
	}

	public boolean acquire(long time, TimeUnit unit) {
		return false;
	}

	public boolean release() {
		return false;
	}

}
