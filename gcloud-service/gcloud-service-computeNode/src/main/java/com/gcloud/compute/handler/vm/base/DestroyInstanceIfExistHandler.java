package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceIfExistMsg;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/12.
 */
@Slf4j
@Handler
public class DestroyInstanceIfExistHandler extends AsyncMessageHandler<DestroyInstanceIfExistMsg> {

    @Autowired
    private IVmBaseNodeService vmBaseNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(DestroyInstanceIfExistMsg msg) {
        DestroyInstanceReplyMsg replyMsg = msg.deriveMsg(DestroyInstanceReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setInstanceId(msg.getInstanceId());
        replyMsg.setSuccess(false);
        replyMsg.setHandleResource(msg.getHandleResource());
        try{
            vmBaseNodeService.destroyIfExist(msg.getInstanceId());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("云服务器destroy异常", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010801::云服务器destroy异常"));
            String vmState = vmBaseNodeService.vmGcloudState(msg.getInstanceId());
            replyMsg.setCurrentState(vmState);
        }
        bus.send(replyMsg);

    }
}
