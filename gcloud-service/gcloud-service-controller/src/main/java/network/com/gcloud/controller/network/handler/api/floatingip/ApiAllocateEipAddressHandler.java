package com.gcloud.controller.network.handler.api.floatingip;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.controller.network.service.IFloatingIpService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.AllocateEipAddressMsg;
import com.gcloud.header.network.msg.api.AllocateEipAddressReplyMsg;

@GcLog(taskExpect="申请弹性公网IP地址成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.EIPADDRSS,action="AllocateEipAddress")
//@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.NETWORK, resourceIdField = "networkId")
public class ApiAllocateEipAddressHandler extends MessageHandler<AllocateEipAddressMsg, AllocateEipAddressReplyMsg> {
	@Autowired
	IFloatingIpService eipService;
	
	@Override
	public AllocateEipAddressReplyMsg handle(AllocateEipAddressMsg msg) throws GCloudException {
		AllocateEipAddressResponse res = eipService.allocateEipAddress(msg.getNetworkId(), msg.getRegion(), msg.getCurrentUser());
		AllocateEipAddressReplyMsg reply = new AllocateEipAddressReplyMsg();
		reply.setAllocationId(res.getAllocationId());
		reply.setEipAddress(res.getEipAddress());
		msg.setObjectId(msg.getNetworkId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.NETWORK_NAME, msg.getNetworkId()));
		return reply;
	}

}
