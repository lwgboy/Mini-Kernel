package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.AssociateInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiAssociateInstanceTypeMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "AssociateInstanceType")
@GcLog(taskExpect = "实例类型和可用区关联")
public class ApiAssociateInstanceTypeHandler extends MessageHandler<ApiAssociateInstanceTypeMsg, ApiReplyMessage>{

	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiReplyMessage handle(ApiAssociateInstanceTypeMsg msg) throws GCloudException {
		AssociateInstanceTypeParams params = BeanUtil.copyProperties(msg, AssociateInstanceTypeParams.class);
		vmBaseService.associateInstanceType(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
