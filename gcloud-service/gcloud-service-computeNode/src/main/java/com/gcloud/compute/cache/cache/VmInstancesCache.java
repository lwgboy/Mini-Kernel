/*
 * @Date 2015-5-15
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.compute.cache.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.common.util.ObjectUtil;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;

//import com.gcloud.service.common.compute.model.VmDetail;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class VmInstancesCache {
	private static ConcurrentNavigableMap<String, VmDetail> vmInstancesCache = new ConcurrentSkipListMap<String, VmDetail>();

	/**
	 * 添加云服务器对象
	 * 
	 * @param instanceId
	 * @param instance
	 */
	public static void add(String instanceId, VmDetail instance) {
		vmInstancesCache.put(instanceId, instance);
		log.debug(String.format("【收养管理-%s-调用添加缓存-%s】%s", ObjectUtil.getCalledMethod(), instanceId, JSON.toJSONString(instance, SerializerFeature.WriteMapNullValue)));
	}

	/**
	 * 移除云服务器对象
	 * 
	 * @param instanceId
	 * @return
	 */
	public static VmDetail remove(String instanceId) {
		VmDetail vm = vmInstancesCache.remove(instanceId);
		log.debug(String.format("【收养管理-%s-调用删除缓存-%s】", ObjectUtil.getCalledMethod(), instanceId));
		return vm;
	}

	/**
	 * 清空云服务器对象
	 */
	public static void clear() {
		vmInstancesCache.clear();
		log.debug(String.format("【收养管理-%s-调用清空缓存】", ObjectUtil.getCalledMethod()));
	}

	/**
	 * 获得指定云服务器对象
	 * 
	 * @param instanceId
	 * @return
	 */
	public static VmDetail get(String instanceId) {
		VmDetail vm = vmInstancesCache.get(instanceId);
		log.debug(String.format("【收养管理-%s-调用查询缓存-%s】%s", ObjectUtil.getCalledMethod(), instanceId, JSON.toJSONString(vm, SerializerFeature.WriteMapNullValue)));
		return vm;
	}

	/**
	 * 获得所有云服务器对象
	 * 
	 *
	 * @return
	 */
	public static Map<String, VmDetail> getAll() {
		return vmInstancesCache;
	}
}
