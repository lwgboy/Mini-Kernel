package com.gcloud.compute.handler.vm.create;

import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.create.ConnectVolumeMsg;
import com.gcloud.header.compute.msg.node.vm.create.ConnectVolumeReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class ConnectVolumeHandler extends AsyncMessageHandler<ConnectVolumeMsg> {

	@Autowired
	private MessageBus bus;

	@Autowired
	private IVmCreateNodeService vmCreateNodeService;
	
	@Override
	public void handle(ConnectVolumeMsg msg) throws GCloudException {
		ConnectVolumeReplyMsg replyMsg = new ConnectVolumeReplyMsg();
		replyMsg.setTaskId(msg.getTaskId());
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setSuccess(true);
		try {
			vmCreateNodeService.connectVolume();
		} catch (Exception e) {
			replyMsg.setErrorCode(e.getMessage());
			replyMsg.setSuccess(false);
		}
		bus.send(replyMsg);
	}

}
