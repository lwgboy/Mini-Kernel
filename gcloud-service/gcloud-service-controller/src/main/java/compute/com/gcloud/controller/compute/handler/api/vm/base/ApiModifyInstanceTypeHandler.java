package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.ModifyInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiModifyInstanceTypeMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "ModifyInstanceType")
@GcLog(taskExpect = "修改实例类型")
public class ApiModifyInstanceTypeHandler extends MessageHandler<ApiModifyInstanceTypeMsg, ApiReplyMessage>{

	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiReplyMessage handle(ApiModifyInstanceTypeMsg msg) throws GCloudException {
		ModifyInstanceTypeParams params = BeanUtil.copyProperties(msg, ModifyInstanceTypeParams.class);
		vmBaseService.modifyInstanceType(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
