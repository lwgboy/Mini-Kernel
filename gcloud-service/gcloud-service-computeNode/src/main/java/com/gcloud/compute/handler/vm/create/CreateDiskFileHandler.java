package com.gcloud.compute.handler.vm.create;

import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.create.CreateDiskFileMsg;
import com.gcloud.header.compute.msg.node.vm.create.CreateDiskFileReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class CreateDiskFileHandler extends AsyncMessageHandler<CreateDiskFileMsg> {

	@Autowired
	private MessageBus bus;

	@Autowired
	private IVmCreateNodeService vmCreateNodeService;
	
	@Override
	public void handle(CreateDiskFileMsg msg) throws GCloudException {
		CreateDiskFileReplyMsg replyMsg = new CreateDiskFileReplyMsg();
		replyMsg.setTaskId(msg.getTaskId());
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setSuccess(true);
		try {
			// TODO vmCreateNodeService.createDiskFile(msg.getStorageType(), msg.getImageId(), msg.getVolumeId(), msg.getSystemSize());
		} catch (Exception e) {
			replyMsg.setErrorCode(e.getMessage());
			replyMsg.setSuccess(false);
		}
		bus.send(replyMsg);
	}

}
