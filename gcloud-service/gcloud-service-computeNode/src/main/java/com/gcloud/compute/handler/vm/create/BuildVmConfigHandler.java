package com.gcloud.compute.handler.vm.create;

import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.create.BuildVmConfigMsg;
import com.gcloud.header.compute.msg.node.vm.create.BuildVmConfigReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class BuildVmConfigHandler extends AsyncMessageHandler<BuildVmConfigMsg>{

	@Autowired
    private IVmCreateNodeService vmCreateNodeService;
	
	@Autowired
	private MessageBus bus;

	@Override
	public void handle(BuildVmConfigMsg msg) {
		BuildVmConfigReplyMsg reply = new BuildVmConfigReplyMsg();
		reply.setServiceId(MessageUtil.controllerServiceId());
		reply.setSuccess(true);
		reply.setTaskId(msg.getTaskId());
		
		try {
			vmCreateNodeService.buildVmConfig(msg.getVmDetail(), msg.getUserId());
		} catch (Exception e) {
			log.error("BuildVmConfigHandler error", e);
			reply.setSuccess(false);
			reply.setErrorCode(ErrorCodeUtil.getErrorCode(e, "::创建虚拟机异常"));
		}
		
		bus.send(reply);
	}
}
