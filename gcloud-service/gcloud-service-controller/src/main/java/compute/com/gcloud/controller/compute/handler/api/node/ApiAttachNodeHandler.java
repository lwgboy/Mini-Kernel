package com.gcloud.controller.compute.handler.api.node;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.model.node.AttachNodeParams;
import com.gcloud.controller.compute.service.node.IComputeNodeService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.node.node.ApiAttachNodeMsg;

@ApiHandler(module = Module.ECS, subModule= SubModule.NODE, action = "AttachNode")
@GcLog(taskExpect = "关联节点和可用区")
public class ApiAttachNodeHandler extends MessageHandler<ApiAttachNodeMsg, ApiReplyMessage>{

	@Autowired
	private IComputeNodeService nodeService;
	
	@Override
	public ApiReplyMessage handle(ApiAttachNodeMsg msg) throws GCloudException {
		AttachNodeParams params = BeanUtil.copyProperties(msg, AttachNodeParams.class);
		nodeService.attachNode(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
