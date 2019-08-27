package com.gcloud.compute.handler.vm.create;

import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.create.CheckCreateVmNodeEnvMsg;
import com.gcloud.header.compute.msg.node.vm.create.CheckCreateVmNodeEnvReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class CheckCreateVmNodeEnvHandler extends AsyncMessageHandler<CheckCreateVmNodeEnvMsg> {

	@Autowired
	private IVmCreateNodeService vmCreateNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(CheckCreateVmNodeEnvMsg msg) throws GCloudException {
		CheckCreateVmNodeEnvReplyMsg replyMsg = new CheckCreateVmNodeEnvReplyMsg();
		replyMsg.setTaskId(msg.getTaskId());
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setSuccess(true);

		try {
			vmCreateNodeService.checkCreateVmNodeEnv(msg.getUserId(), msg.getInstanceId());
		} catch (Exception e) {
			replyMsg.setErrorCode(e.getMessage());
			replyMsg.setSuccess(false);
		}

		bus.send(replyMsg);
	}

}
