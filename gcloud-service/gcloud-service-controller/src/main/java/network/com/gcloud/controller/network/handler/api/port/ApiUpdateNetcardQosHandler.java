package com.gcloud.controller.network.handler.api.port;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.UpdateNetcardQosMsg;

@GcLog(taskExpect="设置网卡带宽成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.NETWORKINTERFACE,action="UpdateNetcardQos")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.PORT, resourceIdField = "networkInterfaceId")
public class ApiUpdateNetcardQosHandler extends MessageHandler<UpdateNetcardQosMsg, ApiReplyMessage> {
	@Autowired
	private IPortService portService;

	@Override
	public ApiReplyMessage handle(UpdateNetcardQosMsg msg) throws GCloudException {
		portService.updateQos(msg.getNetworkInterfaceId(), msg.getOutgress(), msg.getIngress());
		
		msg.setObjectId(msg.getNetworkInterfaceId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.PORT_NAME, msg.getNetworkInterfaceId()));
		return new ApiReplyMessage();
	}

}
