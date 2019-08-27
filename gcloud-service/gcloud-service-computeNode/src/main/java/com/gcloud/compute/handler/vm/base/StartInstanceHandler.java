package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class StartInstanceHandler extends AsyncMessageHandler<StartInstanceMsg>{

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(StartInstanceMsg msg) {
		StartInstanceReplyMsg replyMsg = msg.deriveMsg(StartInstanceReplyMsg.class);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setInstanceId(msg.getInstanceId());
		replyMsg.setSuccess(false);
		replyMsg.setHandleResource(msg.getHandleResource());
		try{
			vmBaseNodeService.startup(msg.getInstanceId());
			replyMsg.setSuccess(true);
		}catch (Exception ex){
			log.error("云服务器开机异常", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "11010206::云服务器开机异常"));
			String vmState = vmBaseNodeService.vmGcloudState(msg.getInstanceId());
			replyMsg.setCurrentState(vmState);
		}
		bus.send(replyMsg);

	}
}
