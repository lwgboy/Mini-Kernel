/*
 * @Date 2015-11-9
 * 
 * @Author chenyu@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.compute.cache.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.gcloud.header.compute.msg.node.vm.model.VmDetail;

public class VmExceptionCache {
    private static ConcurrentNavigableMap<String, VmDetail> vmExceptionCache=new ConcurrentSkipListMap<String,VmDetail>();
    
    /**
     * 添加云服务器对象
     * @param instanceId
     * @param instance
     */
    public static void add(String instanceId,VmDetail instance){
        if(vmExceptionCache.containsKey(instanceId))
            vmExceptionCache.remove(instanceId);
        vmExceptionCache.put(instanceId, instance);
    }
    
    /**
     * 移除云服务器对象
     * @param instanceid
     * @return
     */
    public static VmDetail remove(String instanceid)
    {
        return vmExceptionCache.remove(instanceid);
    }
    
    /**
     * 清空云服务器对象
     */
    public static void clear()
    {
        vmExceptionCache.clear();
    }
    
    /**
     * 获得指定云服务器对象
     * @param instanceId
     * @return
     */
    public static VmDetail get(String instanceId){
        return vmExceptionCache.get(instanceId);
    }
    
    /**
     * 获得所有云服务器对象
     * @param instanceId
     * @return
     */
    public static Map<String,VmDetail> getAll(){
        return vmExceptionCache;
    }
}
