package com.gcloud.controller.compute.handler.node.node;

import com.gcloud.controller.compute.service.node.IComputeNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.node.ComputeNodeConnectMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/10/18.
 */
@Handler
public class ComputeNodeConnectHandler extends AsyncMessageHandler<ComputeNodeConnectMsg> {

    @Autowired
    private IComputeNodeService computeNodeService;

    @Override
    public void handle(ComputeNodeConnectMsg msg) {
        computeNodeService.computeNodeConnect(msg.getComputeNodeInfo(), msg.getNodeTimeout());
    }
}
