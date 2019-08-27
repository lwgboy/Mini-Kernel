package com.gcloud.controller.network.handler.api.subnet;

import com.gcloud.controller.network.service.ISwitchService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.msg.api.CreateVSwitchMsg;
import com.gcloud.header.network.msg.api.CreateVSwitchReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="创建交换机成功")
@ApiHandler(module=Module.ECS,subModule=SubModule.VSWITCH,action="CreateVSwitch")
public class ApiCreateVSwitchHandler extends MessageHandler<CreateVSwitchMsg, CreateVSwitchReplyMsg> {
	
	@Autowired
	private ISwitchService switchService;
	
	@Override
	public CreateVSwitchReplyMsg handle(CreateVSwitchMsg msg) throws GCloudException {
		CreateVSwitchReplyMsg reply = new CreateVSwitchReplyMsg();
		reply.setvSwitchId(switchService.createVSwitch(msg, msg.getCurrentUser()));
//		msg.setObjectId(reply.getvSwitchId());
//		msg.setObjectName(msg.getvSwitchName());
		return reply;
	}

}
