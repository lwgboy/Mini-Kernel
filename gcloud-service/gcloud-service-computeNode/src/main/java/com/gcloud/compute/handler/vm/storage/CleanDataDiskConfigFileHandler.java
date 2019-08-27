package com.gcloud.compute.handler.vm.storage;

import com.gcloud.compute.service.vm.storage.IVmVolumeNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.storage.CleanDataDiskConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.storage.CleanDataDiskConfigFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class CleanDataDiskConfigFileHandler extends AsyncMessageHandler<CleanDataDiskConfigFileMsg> {

	@Autowired
	private IVmVolumeNodeService vmVolumeNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(CleanDataDiskConfigFileMsg msg) {

		CleanDataDiskConfigFileReplyMsg replyMsg = msg.deriveMsg(CleanDataDiskConfigFileReplyMsg.class);
		replyMsg.setSuccess(false);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		try {
			vmVolumeNodeService.cleanDataDiskFile(msg.getInstanceId(), msg.getVolumeDetail());
			replyMsg.setSuccess(true);
		} catch (Exception ex) {
			log.error("删除磁盘文件错误", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1011001::删除磁盘文件错误"));
		}
		bus.send(replyMsg);

	}
}
