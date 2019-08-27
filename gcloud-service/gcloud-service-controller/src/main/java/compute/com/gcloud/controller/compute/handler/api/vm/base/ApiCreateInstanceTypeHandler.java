package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.CreateInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiCreateInstanceTypeMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "CreateInstanceType")
@GcLog(taskExpect = "创建实例类型")
public class ApiCreateInstanceTypeHandler extends MessageHandler<ApiCreateInstanceTypeMsg, ApiReplyMessage>{

	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiReplyMessage handle(ApiCreateInstanceTypeMsg msg) throws GCloudException {
		CreateInstanceTypeParams params = BeanUtil.copyProperties(msg, CreateInstanceTypeParams.class);
		vmBaseService.createInstanceType(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
