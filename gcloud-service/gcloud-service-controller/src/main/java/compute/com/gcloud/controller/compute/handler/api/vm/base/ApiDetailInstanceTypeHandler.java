package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.vm.DetailInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.DetailInstanceType;
import com.gcloud.header.compute.msg.api.vm.base.ApiDetailInstanceTypeMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiDetailInstanceTypeReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "DetailInstanceType")
@GcLog(taskExpect = "实例类型详情")
public class ApiDetailInstanceTypeHandler extends MessageHandler<ApiDetailInstanceTypeMsg, ApiDetailInstanceTypeReplyMsg>{

	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiDetailInstanceTypeReplyMsg handle(ApiDetailInstanceTypeMsg msg) throws GCloudException {
		DetailInstanceTypeParams params = BeanUtil.copyProperties(msg, DetailInstanceTypeParams.class);
		DetailInstanceType instanceType = vmBaseService.detailInstanceType(params, msg.getCurrentUser());
		
		ApiDetailInstanceTypeReplyMsg reply = new ApiDetailInstanceTypeReplyMsg();
		reply.setDetailInstanceType(instanceType);
		return reply;
	}

}
