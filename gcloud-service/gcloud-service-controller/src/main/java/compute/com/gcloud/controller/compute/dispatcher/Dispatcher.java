package com.gcloud.controller.compute.dispatcher;

import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.node.ResourceUnit;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;

/**
 * Created by yaowj on 2019/1/7.
 */
public abstract class Dispatcher {

    public abstract Node assignNode(Integer core, Integer memory);

    public abstract Node assignNodeInZone(String zoneId, Integer core, Integer memory);

    public abstract void assignNode(String hostname, Integer core, Integer memory);

    public void release(String hostname, Integer core, Integer memory){
        RedisNodesUtil.releaseCompute(hostname, core, memory);
    }

    protected void allocateCompute(int core, int memory, String hostname){

        ResourceUnit ru = RedisNodesUtil.allocateCompute(hostname, core, memory);
        if (ru == null) {
            throw new GCloudException("compute_controller_drs_020001::Node not registered or not existed");
        } else if (ru.getCore() < 0) {
            throw new GCloudException("compute_controller_drs_020002::Node CPU resource is not sufficient ");
        } else if (ru.getMemory() < 0) {
            throw new GCloudException("compute_controller_drs_020003::Node Memory resource is not sufficient ");
        }
    }

    public Boolean checkResource(String hostName, int core, int memory) {
        ResourceUnit ru = RedisNodesUtil.checkComputeNode(hostName, core, memory);
        if (ru == null || ru.getCore() < 0 || ru.getMemory() < 0) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean occupyResource(int core, int memory, String hostName) {
        ResourceUnit ru = RedisNodesUtil.allocateCompute(hostName, core, memory);
        return ru != null && ru.getCore() >= 0 && ru.getMemory() >= 0;
    }

    public static Dispatcher dispatcher(DispatcherSelector selector){
        return (Dispatcher) SpringUtil.getBean(selector.getImpl());
    }

    //获取默认的，根据配置文件配置
    public static Dispatcher dispatcher(){
        return SpringUtil.getBean(Dispatcher.class);
    }
}
