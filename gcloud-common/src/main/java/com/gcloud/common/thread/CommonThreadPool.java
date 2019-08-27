/*
 * @Date 2015-12-16
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 公共线程池
 */
package com.gcloud.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommonThreadPool {
	private static final int POOL_SIZE = 30;
	private static ExecutorService fixedThreadPool = Executors
			.newFixedThreadPool(POOL_SIZE);

	public static ExecutorService getThreadPool() {
		return fixedThreadPool;
	}
}
