package com.gcloud.compute.timer;

import com.gcloud.compute.service.vm.base.IVmAdoptNodeService;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;
import com.gcloud.header.compute.msg.node.vm.base.SyncStateMsg;
import com.gcloud.header.compute.msg.node.vm.model.VmStateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yaowj on 2019/1/9.
 */
@Component
@Slf4j
public class InstanceStateTimer {

    @Autowired
    private IVmAdoptNodeService vmAdoptNodeService;

    @Autowired
    private MessageBus bus;

    @Autowired
    private ComputeNodeInfo nodeInfo;

    @Scheduled(fixedDelay = 10000L)
    public void refresh(){

        Map<String, VmStateInfo> stateInfos = vmAdoptNodeService.stateInfo();
        SyncStateMsg msg = new SyncStateMsg();
        msg.setServiceId(MessageUtil.controllerServiceId());
        msg.setStateInfos(stateInfos);
        msg.setHostname(nodeInfo.getHostname());

        bus.send(msg);

    }

}
