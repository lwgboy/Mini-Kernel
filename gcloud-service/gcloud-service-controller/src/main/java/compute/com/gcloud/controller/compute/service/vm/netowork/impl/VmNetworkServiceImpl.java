package com.gcloud.controller.compute.service.vm.netowork.impl;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.enums.OvsBridgeRefType;
import com.gcloud.controller.network.service.IOvsBridgeService;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.VmTaskState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yaowj on 2018/11/15.
 */
@Service
@Slf4j
public class VmNetworkServiceImpl implements IVmNetworkService {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private PortDao portDao;

    @Autowired
    private IPortService portService;

    @Autowired
    private IOvsBridgeService ovsBridgeService;

    @Transactional
    public void attachPortInit(String instanceId, String portId, String customOvsBr, Boolean noArpLimit, boolean inTask) {
        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010603::找不到对应的云服务器");
        }

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0010604::找不到对应的网卡");
        }

        Node node = RedisNodesUtil.getComputeNodeByHostName(vm.getHostname());
        if(node == null){
            throw new GCloudException("0010605::节点不存在");
        }

        if(!inTask){
            if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.ATTACH_NETCARD)) {
                throw new GCloudException("0010606::云服务器当前状态不能挂载网卡");
            }
        }

        portService.attachPort(vm, portId, customOvsBr, noArpLimit);

    }

    @Override
    public void detachPortInit(String instanceId, String portId, boolean inTask) {

        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010703::找不到对应的云服务器");
        }

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0010704::找不到对应的网卡");
        }

        if(port.getDeviceId() == null || !port.getDeviceId().equals(instanceId)){
            throw new GCloudException("0010705::此云服务器没有挂载此网卡，不能卸载");
        }

        Node node = RedisNodesUtil.getComputeNodeByHostName(vm.getHostname());
        if(node == null){
            throw new GCloudException("0010706::节点不存在");
        }

        if(!inTask){
            if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.DETACH_NETCARD)) {
                throw new GCloudException("0010707::云服务器当前状态不能卸载网卡");
            }
        }
    }

    @Override
    public void detachDone(String instanceId, String portId, boolean inTask) {
        vmBaseService.cleanState(instanceId, inTask);
        //需要获取

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0010708::网卡不存在");
        }

        ovsBridgeService.release(OvsBridgeRefType.PORT, portId);
        portService.detachDone(port);



    }
}
