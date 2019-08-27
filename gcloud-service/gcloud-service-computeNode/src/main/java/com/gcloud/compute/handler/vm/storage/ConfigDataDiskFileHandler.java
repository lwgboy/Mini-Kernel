package com.gcloud.compute.handler.vm.storage;

import com.gcloud.compute.service.vm.storage.IVmVolumeNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.storage.ConfigDataDiskFileMsg;
import com.gcloud.header.compute.msg.node.vm.storage.ConfigDataDiskFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class ConfigDataDiskFileHandler extends AsyncMessageHandler<ConfigDataDiskFileMsg> {

	@Autowired
	private IVmVolumeNodeService vmVolumeNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(ConfigDataDiskFileMsg msg) {

		ConfigDataDiskFileReplyMsg replyMsg = msg.deriveMsg(ConfigDataDiskFileReplyMsg.class);
		replyMsg.setSuccess(false);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		try {
			vmVolumeNodeService.configDataDiskFile(msg.getInstanceId(), msg.getVolumeDetail());
			replyMsg.setSuccess(true);
		} catch (Exception ex) {
			log.error("配置磁盘文件错误", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010901::配置磁盘文件错误"));
		}
		bus.send(replyMsg);

	}
}
