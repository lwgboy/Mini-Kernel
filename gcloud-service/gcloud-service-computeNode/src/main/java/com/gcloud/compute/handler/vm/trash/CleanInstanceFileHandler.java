package com.gcloud.compute.handler.vm.trash;

import com.gcloud.compute.service.vm.trash.IVmTrashNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceFileMsg;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/28.
 */
@Handler
@Slf4j
public class CleanInstanceFileHandler extends AsyncMessageHandler<CleanInstanceFileMsg> {

    @Autowired
    private IVmTrashNodeService vmTrashNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(CleanInstanceFileMsg msg) {

        CleanInstanceFileReplyMsg replyMsg = msg.deriveMsg(CleanInstanceFileReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmTrashNodeService.cleanInstanceFile(msg.getInstanceId());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("清理云服务器文件失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::清理云服务器文件失败"));
        }
        bus.send(replyMsg);

    }



}
