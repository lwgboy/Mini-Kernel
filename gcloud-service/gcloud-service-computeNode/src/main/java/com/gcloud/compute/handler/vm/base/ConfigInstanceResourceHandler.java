package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.ConfigInstanceResourceMsg;
import com.gcloud.header.compute.msg.node.vm.base.ConfigInstanceResourceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/12.
 */
@Slf4j
@Handler
public class ConfigInstanceResourceHandler extends AsyncMessageHandler<ConfigInstanceResourceMsg> {

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Autowired
	private MessageBus bus;

	@Override
	public void handle(ConfigInstanceResourceMsg msg) {
		msg.getServiceId();
		ConfigInstanceResourceReplyMsg replyMsg = msg.deriveMsg(ConfigInstanceResourceReplyMsg.class);
		replyMsg.setServiceId(MessageUtil.controllerServiceId());
		replyMsg.setSuccess(false);
		try {
			vmBaseNodeService.configInstanceResource(msg.getInstanceId(), msg.getCpu(), msg.getMemory(), msg.getOrgCpu(), msg.getOrgMemory());
			replyMsg.setSuccess(true);
		} catch (Exception ex) {
			log.error("修改云服务器配置异常", ex);
			replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1011301::修改云服务器配置异常"));
		}
		bus.send(replyMsg);

	}
}
