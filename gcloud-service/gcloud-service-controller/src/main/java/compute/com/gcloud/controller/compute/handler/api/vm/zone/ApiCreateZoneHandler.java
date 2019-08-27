
package com.gcloud.controller.compute.handler.api.vm.zone;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.zone.ApiCreateZoneMsg;
import com.gcloud.header.compute.msg.api.vm.zone.ApiCreateZoneReplyMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "CreateZone")
public class ApiCreateZoneHandler extends MessageHandler<ApiCreateZoneMsg, ApiCreateZoneReplyMsg> {

    @Autowired
    private IVmZoneService zoneService;

    @Override
    public ApiCreateZoneReplyMsg handle(ApiCreateZoneMsg msg) throws GCloudException {
        ApiCreateZoneReplyMsg replyMessage = new ApiCreateZoneReplyMsg();
        this.zoneService.createZone(msg.getZoneName());
        return replyMessage;
    }

}
