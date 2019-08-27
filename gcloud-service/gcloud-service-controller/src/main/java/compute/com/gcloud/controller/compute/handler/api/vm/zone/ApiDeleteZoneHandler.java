package com.gcloud.controller.compute.handler.api.vm.zone;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.DeleteZoneParams;
import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.zone.ApiDeleteZoneMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "DeleteZone")
@GcLog(taskExpect = "删除可用区")
public class ApiDeleteZoneHandler extends MessageHandler<ApiDeleteZoneMsg, ApiReplyMessage>{

	@Autowired
    private IVmZoneService zoneService;
	
	@Override
	public ApiReplyMessage handle(ApiDeleteZoneMsg msg) throws GCloudException {
		DeleteZoneParams params = BeanUtil.copyProperties(msg, DeleteZoneParams.class);
		zoneService.deleteZone(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
