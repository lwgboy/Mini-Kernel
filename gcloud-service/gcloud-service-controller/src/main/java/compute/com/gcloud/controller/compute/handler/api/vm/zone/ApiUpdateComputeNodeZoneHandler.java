
package com.gcloud.controller.compute.handler.api.vm.zone;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.zone.ApiUpdateComputeNodeZoneMsg;
import com.gcloud.header.compute.msg.api.vm.zone.ApiUpdateComputeNodeZoneReplyMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "UpdateComputeNodeZone")
public class ApiUpdateComputeNodeZoneHandler extends MessageHandler<ApiUpdateComputeNodeZoneMsg, ApiUpdateComputeNodeZoneReplyMsg> {

    @Autowired
    private IVmZoneService zoneService;

    @Override
    public ApiUpdateComputeNodeZoneReplyMsg handle(ApiUpdateComputeNodeZoneMsg msg) throws GCloudException {
        ApiUpdateComputeNodeZoneReplyMsg replyMessage = new ApiUpdateComputeNodeZoneReplyMsg();
        this.zoneService.updateComputeNodeZone(msg.getZoneId(), msg.getNodeIds());
        return replyMessage;
    }

}
