package com.gcloud.compute.timer;

import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.node.ComputeNodeConnectMsg;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/10/18.
 */
@Component
@Slf4j
public class ConnectControllerTimer {

    @Autowired
    private MessageBus bus;

    @Autowired
    private ComputeNodeProp prop;

    @Autowired
    private ComputeNodeInfo nodeInfo;

    @Scheduled(fixedDelayString = "${gcloud.computeNode.reportFrequency:30}" + "000")
    public void connect(){

        log.debug("ConnectControllerTimer begin");

        try {
            ComputeNodeConnectMsg msg = new ComputeNodeConnectMsg();
            msg.setServiceId(MessageUtil.controllerServiceId());
            msg.setComputeNodeInfo(nodeInfo);
            msg.setNodeTimeout(prop.getReportFrequency() * 2);
            bus.send(msg);

        } catch (Exception e) {
            log.error("连接控制器失败", e);
        }

        log.debug("ConnectControllerTimer end");

    }

}
