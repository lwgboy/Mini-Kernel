package com.gcloud.compute.handler.vm.storage;

import com.gcloud.compute.service.vm.storage.IVmVolumeNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.storage.ForceCleanDiskConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.storage.ForceCleanDiskConfigFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class ForceCleanDiskConfigFileHandler extends AsyncMessageHandler<ForceCleanDiskConfigFileMsg> {

	@Autowired
	private IVmVolumeNodeService vmVolumeNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(ForceCleanDiskConfigFileMsg msg) {

		ForceCleanDiskConfigFileReplyMsg replyMsg = msg.deriveMsg(ForceCleanDiskConfigFileReplyMsg.class);
		replyMsg.setSuccess(false);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		try {
			vmVolumeNodeService.forceCleanDataDiskFile(msg.getInstanceId(), msg.getVolumeId());
			replyMsg.setSuccess(true);
		} catch (Exception ex) {
			log.error("删除磁盘文件错误", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010802::清除磁盘文件失败"));
		}
		bus.send(replyMsg);

	}
}
