package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 上下文Bean获取 <br>
 * 版权: Copyright (c) 2011-2019<br>
 * 
 * @author: 孙常军<br>
 * @date: 2019年6月21日<br>
 */
public class ContextHolder {
	public static ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:app-*.xml");
}
