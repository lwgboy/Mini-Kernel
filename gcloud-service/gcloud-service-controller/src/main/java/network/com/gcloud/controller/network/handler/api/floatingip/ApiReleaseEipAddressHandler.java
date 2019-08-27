package com.gcloud.controller.network.handler.api.floatingip;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IFloatingIpService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ReleaseEipAddressMsg;

@GcLog(taskExpect="释放弹性公网IP地址")
@ApiHandler(module=Module.ECS,subModule=SubModule.EIPADDRSS,action="ReleaseEipAddress")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.EIP, resourceIdField = "allocationId")
public class ApiReleaseEipAddressHandler extends MessageHandler<ReleaseEipAddressMsg, ApiReplyMessage> {
	@Autowired
	IFloatingIpService eipService;
	
	@Override
	public ApiReplyMessage handle(ReleaseEipAddressMsg msg) throws GCloudException {
		eipService.releaseEipAddress(msg.getAllocationId());
		
		msg.setObjectId(msg.getAllocationId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.EIP_NAME, msg.getAllocationId()));
		return new ApiReplyMessage();
	}

}
