package com.gcloud.controller.network.handler.api.port;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateNetworkInterfaceMsg;
import com.gcloud.header.network.msg.api.CreateNetworkInterfaceReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@GcLog(taskExpect="新增网卡成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.NETWORKINTERFACE,action="CreateNetworkInterface")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SUBNET, resourceIdField = "vSwitchId")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SECURITYGROUP, resourceIdField = "securityGroupId")
public class ApiCreateNetworkInterfaceHandler extends MessageHandler<CreateNetworkInterfaceMsg, CreateNetworkInterfaceReplyMsg> {
	@Autowired
	private IPortService portService;

	@Override
	public CreateNetworkInterfaceReplyMsg handle(CreateNetworkInterfaceMsg msg) throws GCloudException {
		CreateNetworkInterfaceReplyMsg reply = new CreateNetworkInterfaceReplyMsg();
        CreatePortParams params = new CreatePortParams();
        params.setSubnetId(msg.getvSwitchId());
        params.setName(msg.getNetworkInterfaceName());
        params.setIpAddress(msg.getPrimaryIpAddress());
        params.setSecurityGroupId(msg.getSecurityGroupId());
        params.setDescription(msg.getDescription());
		reply.setNetworkInterfaceId(portService.create(params, msg.getCurrentUser()));
		return reply;
	}

}
