package com.gcloud.controller.compute.handler.api.vm.zone;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.DetailZoneParams;
import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.DetailZone;
import com.gcloud.header.compute.msg.api.vm.zone.ApiDetailZoneMsg;
import com.gcloud.header.compute.msg.api.vm.zone.ApiDetailZoneReplyMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "DetailZone")
@GcLog(taskExpect = "可用区详情")
public class ApiDetailZoneHandler extends MessageHandler<ApiDetailZoneMsg, ApiDetailZoneReplyMsg>{

	@Autowired
    private IVmZoneService zoneService;
	
	@Override
	public ApiDetailZoneReplyMsg handle(ApiDetailZoneMsg msg) throws GCloudException {
		DetailZoneParams params = BeanUtil.copyProperties(msg, DetailZoneParams.class);
		DetailZone zone = zoneService.detailZone(params, msg.getCurrentUser());
		
		ApiDetailZoneReplyMsg reply = new ApiDetailZoneReplyMsg();
		reply.setDetailZone(zone);
		return reply;
	}

}
