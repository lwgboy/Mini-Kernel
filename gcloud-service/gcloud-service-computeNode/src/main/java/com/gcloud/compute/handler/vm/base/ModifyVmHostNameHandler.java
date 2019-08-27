package com.gcloud.compute.handler.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.ModifyVmHostNameMsg;
import com.gcloud.header.compute.msg.node.vm.base.ModifyVmHostNameReplyMsg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Handler
public class ModifyVmHostNameHandler  extends AsyncMessageHandler<ModifyVmHostNameMsg>{
	@Autowired
	private IVmBaseNodeService vmBaseNodeService;
	
	@Autowired
    private MessageBus bus;
	
	@Override
	public void handle(ModifyVmHostNameMsg msg) {
		ModifyVmHostNameReplyMsg replyMsg = msg.deriveMsg(ModifyVmHostNameReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
        	vmBaseNodeService.changeHostName(msg.getInstanceId(), msg.getHostName());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("::修改实例名称失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::修改实例名称失败"));
        }
        bus.send(replyMsg);
	}

}
