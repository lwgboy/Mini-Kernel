package com.gcloud.compute.handler.vm.trash;

import com.gcloud.compute.service.vm.trash.IVmTrashNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceInfoMsg;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceInfoReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/28.
 */
@Slf4j
@Handler
public class CleanInstanceInfoHandler extends AsyncMessageHandler<CleanInstanceInfoMsg> {

    @Autowired
    private IVmTrashNodeService vmTrashNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(CleanInstanceInfoMsg msg) {

        CleanInstanceInfoReplyMsg replyMsg = msg.deriveMsg(CleanInstanceInfoReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmTrashNodeService.cleanInstanceInfo(msg.getInstanceId());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("清理虚拟机信息失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::清理虚拟机信息失败"));
        }
        bus.send(replyMsg);

    }
}
