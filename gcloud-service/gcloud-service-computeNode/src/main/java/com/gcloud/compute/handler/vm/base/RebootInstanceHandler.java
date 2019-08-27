package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.RebootInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.RebootInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class RebootInstanceHandler extends AsyncMessageHandler<RebootInstanceMsg>{

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(RebootInstanceMsg msg) {
		RebootInstanceReplyMsg replyMsg = msg.deriveMsg(RebootInstanceReplyMsg.class);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setInstanceId(msg.getInstanceId());
		replyMsg.setSuccess(false);
		try{
			vmBaseNodeService.reboot(msg.getInstanceId(), msg.getForceStop());
			replyMsg.setSuccess(true);
		}catch (Exception ex){
			log.error("云服务器重启异常", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010408::云服务器重启异常"));
			String vmState = vmBaseNodeService.vmGcloudState(msg.getInstanceId());
			replyMsg.setCurrentState(vmState);
		}
		bus.send(replyMsg);

	}
}
