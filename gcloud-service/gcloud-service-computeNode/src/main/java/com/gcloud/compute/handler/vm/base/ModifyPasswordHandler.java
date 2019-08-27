package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.ModifyPasswordMsg;
import com.gcloud.header.compute.msg.node.vm.base.ModifyPasswordReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class ModifyPasswordHandler extends AsyncMessageHandler<ModifyPasswordMsg>{
	@Autowired
	private IVmBaseNodeService vmBaseNodeService;
	
	@Autowired
    private MessageBus bus;
    
	@Override
	public void handle(ModifyPasswordMsg msg) {
		ModifyPasswordReplyMsg replyMsg = msg.deriveMsg(ModifyPasswordReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
        	vmBaseNodeService.changePassword(msg.getInstanceId(), msg.getPassword());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("::修改实例密码失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::修改实例密码失败"));
        }
        bus.send(replyMsg);
	}
}
