package com.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static ApplicationContext applicationContext;

	public static void main(String[] args) {
		try {
			logger.info("程序启动开始");
			applicationContext = new ClassPathXmlApplicationContext("classpath*:app-*.xml");
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
}
