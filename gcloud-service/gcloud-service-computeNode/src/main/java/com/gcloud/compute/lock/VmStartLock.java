package com.gcloud.compute.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;

/*
 * @Date 2016-1-23
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2016 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
/*
 * @Date 2016-1-23
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2016 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
public class VmStartLock {
	private static Logger LOG = LogManager.getLogger(VmStartLock.class);

	private static Map<String, Date> vmStartLock = new ConcurrentHashMap<String, Date>();
	private static List<String> vmStartList = Collections.synchronizedList(new ArrayList<String>());
	private final static long SLEEP_TIME = 5000L;

	public static Map<String, Date> getLock () {
		return vmStartLock;
	}
	
	/**
	 * 
	 * @Title: addConcurrentNum
	 * @Description: 创建同步锁，返回true表示可以创建，返回false标识需要等待
	 * @date 2016-1-23 上午11:44:12
	 * 
	 * @param instanceId
	 * @return
	 */
	public static synchronized boolean addConcurrentNum(String instanceId) {
		ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
		int maxNum = prop.getVmStartMaxNum();
		if (maxNum <= 0) {
			return true;
		}

		if (vmStartLock.size() < maxNum) {
			vmStartLock.put(instanceId, new Date());
			LOG.debug(String.format("【检测开机序列】加入同步锁【%s】，当前锁数量【%s】", instanceId, vmStartLock.size()));
			return true;
		} else {
			LOG.debug(String.format("【检测开机序列】同步锁已满，等待加入【%s】，当前锁数量【%s】", instanceId, vmStartLock.size()));
			return false;
		}
	}

	/**
	 * 
	 * @Title: removeConcurrentNum
	 * @Description: 删除同步锁
	 * @date 2016-1-23 上午11:46:42
	 * 
	 * @param instanceId
	 */
	public static synchronized void removeConcurrentNum(String instanceId) {
		vmStartLock.remove(instanceId);
		LOG.debug(String.format("【检测开机序列】移除同步锁【%s】，当前锁数量【%s】", instanceId, vmStartLock.size()));
	}

	/**
	 * @Title: checkVmStartLock
	 * @Description: 创建同步序列
	 * @date 2016-1-23 下午1:14:16
	 * 
	 * @throws InterruptedException
	 */
	public static void checkVmStartLock(String instanceId) throws GCloudException {

		while (true) {
			//map中有，就可以执行
			if (vmStartLock.containsKey(instanceId)) {
				LOG.debug(String.format("【检测开机序列】获取开机同步锁成功【%s】", instanceId));
				break;
			//map中没有，list中也没有则抛错
			} else if(!vmStartList.contains(instanceId)){
				throw new GCloudException("获取开机并发锁失败");
			}else {
				try {
					LOG.debug(String.format("【检测开机序列】等待获取开机同步锁【%s】", instanceId));
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					LOG.error("创建同步序列失败", e);
				}
			}
		}

	}
	
	public static synchronized void addStartList(String instanceId){
		if(!StringUtils.isBlank(instanceId)){
			vmStartList.add(instanceId);
		}
	}
	
	public static synchronized String getFirst(){
		String result = "";
		if(vmStartList != null && vmStartList.size() > 0){
			result = vmStartList.get(0); 
		}
		return result;
	}
	
	public static synchronized void removeStartList(String instanceId){		
		vmStartList.remove(instanceId);
	}

	
}
