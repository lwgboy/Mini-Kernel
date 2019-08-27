package com.gcloud.controller.compute.handler.api.vm.base;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiQueryInstancesVNCMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiQueryInstancesVNCReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module = Module.ECS, subModule= SubModule.VM, action = "DescribeInstanceVncUrl")
public class ApiQueryInstanceVNCHandler extends MessageHandler<ApiQueryInstancesVNCMsg, ApiQueryInstancesVNCReplyMsg> {

    @Autowired
    private IVmBaseService vmBaseService;

    @Override
    public ApiQueryInstancesVNCReplyMsg handle(ApiQueryInstancesVNCMsg msg) throws GCloudException {
        String url = vmBaseService.queryInstanceVNC(msg.getInstanceId());
        ApiQueryInstancesVNCReplyMsg replyMsg = new ApiQueryInstancesVNCReplyMsg();

        replyMsg.setVncUrl(url);
        replyMsg.setSuccess(true);
        // need set request id ?
        return replyMsg;
    }
}
