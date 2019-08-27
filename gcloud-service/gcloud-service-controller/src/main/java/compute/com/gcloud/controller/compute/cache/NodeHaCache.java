/*
 * @Date 2016-8-19
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.controller.compute.cache;

import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.service.common.Consts;

import java.text.MessageFormat;
import java.time.Duration;

public class NodeHaCache {

	
	public static void addNodeHa(String hostName, int timeout) throws GCloudException {
		GCloudRedisTemplate redisTemplate = SpringUtil.getApplicationContext().getBean(GCloudRedisTemplate.class);
		String setName = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODE_HA_CACHE, hostName);
		redisTemplate.opsForValue().set(setName, hostName, Duration.ofSeconds(timeout));
	}

	public static void removeNodeHa(String hostName) throws GCloudException {
		GCloudRedisTemplate redisTemplate = SpringUtil.getApplicationContext().getBean(GCloudRedisTemplate.class);
		String setName = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODE_HA_CACHE, hostName);
		redisTemplate.delete(setName);
	}
	
	public static String getNodeHa(String hostName) throws GCloudException {
		GCloudRedisTemplate redisTemplate = SpringUtil.getApplicationContext().getBean(GCloudRedisTemplate.class);
		String setName = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODE_HA_CACHE, hostName);
		return redisTemplate.opsForValue().get(setName);
	}
}
