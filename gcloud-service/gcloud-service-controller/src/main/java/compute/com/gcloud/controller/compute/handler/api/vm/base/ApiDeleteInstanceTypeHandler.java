package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.DeleteInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiDeleteInstanceTypeMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "DeleteInstanceType")
@GcLog(taskExpect = "删除实例类型")
public class ApiDeleteInstanceTypeHandler extends MessageHandler<ApiDeleteInstanceTypeMsg, ApiReplyMessage>{

	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiReplyMessage handle(ApiDeleteInstanceTypeMsg msg) throws GCloudException {
		DeleteInstanceTypeParams params = BeanUtil.copyProperties(msg, DeleteInstanceTypeParams.class);
		vmBaseService.deleteInstanceType(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
