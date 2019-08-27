package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.StopInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StopInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class StopInstanceHandler extends AsyncMessageHandler<StopInstanceMsg>{

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(StopInstanceMsg msg) {
		StopInstanceReplyMsg replyMsg = msg.deriveMsg(StopInstanceReplyMsg.class);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setInstanceId(msg.getInstanceId());
		replyMsg.setSuccess(false);
		replyMsg.setHandleResource(msg.getHandleResource());
		try{
			vmBaseNodeService.stop(msg.getInstanceId());
			replyMsg.setSuccess(true);
		}catch (Exception ex){
			log.error("云服务器关机异常", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010305::云服务器关机异常"));
			String vmState = vmBaseNodeService.vmGcloudState(msg.getInstanceId());
			replyMsg.setCurrentState(vmState);
		}
		bus.send(replyMsg);

	}
}
