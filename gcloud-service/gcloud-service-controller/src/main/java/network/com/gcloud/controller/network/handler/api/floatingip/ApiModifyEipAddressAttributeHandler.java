package com.gcloud.controller.network.handler.api.floatingip;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.ModifyEipAddressAttributeParams;
import com.gcloud.controller.network.service.IFloatingIpService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.ModifyEipAddressAttributeMsg;


@GcLog(taskExpect="修改弹性公网IP地址属性成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.EIPADDRSS,action="ModifyEipAddressAttribute")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.EIP, resourceIdField = "allocationId")
public class ApiModifyEipAddressAttributeHandler extends MessageHandler<ModifyEipAddressAttributeMsg, ApiReplyMessage> {
	@Autowired
	IFloatingIpService eipService;
	
	@Override
	public ApiReplyMessage handle(ModifyEipAddressAttributeMsg msg) throws GCloudException {
		ModifyEipAddressAttributeParams param = BeanUtil.copyProperties(msg, ModifyEipAddressAttributeParams.class);
		eipService.modifyEipAddressAttribute(param);
		
		msg.setObjectId(msg.getAllocationId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.EIP_NAME, msg.getAllocationId()));
		return new ApiReplyMessage();
	}

}
