package com.gcloud.compute.handler.vm.create;

import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.create.CreateDomainMsg;
import com.gcloud.header.compute.msg.node.vm.create.CreateDomainReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class CreateDomainHandler extends AsyncMessageHandler<CreateDomainMsg> {
	@Autowired
	private IVmCreateNodeService vmCreateNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(CreateDomainMsg msg) {
		CreateDomainReplyMsg reply = new CreateDomainReplyMsg();
		reply.setServiceId(MessageUtil.controllerServiceId());
		reply.setSuccess(true);
		reply.setTaskId(msg.getTaskId());

		try {
			vmCreateNodeService.createDomain(msg.getInstanceId(), msg.getUserId());
		} catch (Exception e) {
			reply.setSuccess(false);
			reply.setErrorCode(e.getMessage());
		}

		bus.send(reply);
	}

}
