package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiModifyInstanceAttributeMsg;

@GcLog(taskExpect = "修改实例属性")
@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "ModifyInstanceAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
public class ApiModifyInstanceAttributeHandler extends MessageHandler<ApiModifyInstanceAttributeMsg, ApiReplyMessage>{
	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiReplyMessage handle(ApiModifyInstanceAttributeMsg msg) throws GCloudException {
		vmBaseService.modifyInstanceAttribute(msg.getInstanceId(), msg.getInstanceName(), msg.getPassword(), null);
		
		msg.setObjectId(msg.getInstanceId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.INSTANCE_ALIAS, msg.getInstanceId()));
		return new ApiReplyMessage();
	}

}
