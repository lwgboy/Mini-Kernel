package com.gcloud.controller.network.handler.api.port;

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
import com.gcloud.header.network.msg.api.ModifyNetworkInterfaceAttributeMsg;
import org.springframework.beans.factory.annotation.Autowired;

@GcLog(taskExpect="修改网卡属性成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.NETWORKINTERFACE,action="ModifyNetworkInterfaceAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.PORT, resourceIdField = "networkInterfaceId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUP, resourceIdField = "securityGroupIds")
public class ApiModifyNetworkInterfaceAttributeHandler extends MessageHandler<ModifyNetworkInterfaceAttributeMsg, ApiReplyMessage> {
	@Autowired
	private IPortService portService;

	@Override
	public ApiReplyMessage handle(ModifyNetworkInterfaceAttributeMsg msg) throws GCloudException {
		portService.update(msg.getNetworkInterfaceId(), msg.getSecurityGroupIds(), msg.getNetworkInterfaceName());
		
		msg.setObjectId(msg.getNetworkInterfaceId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.PORT_NAME, msg.getNetworkInterfaceId()));
		return new ApiReplyMessage();
	}

}
