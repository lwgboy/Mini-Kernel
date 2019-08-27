package com.gcloud.compute.handler.vm.senior;

import com.gcloud.compute.service.vm.senior.IVmSeniorNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.senior.DeleteBundleMsg;
import com.gcloud.header.compute.msg.node.vm.senior.DeleteBundleReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/30.
 */
@Slf4j
@Handler
public class DeleteBundleHandler extends AsyncMessageHandler<DeleteBundleMsg> {

    @Autowired
    private IVmSeniorNodeService vmSeniorNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(DeleteBundleMsg msg) {

        DeleteBundleReplyMsg replyMsg = msg.deriveMsg(DeleteBundleReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmSeniorNodeService.deleteBundle(msg.getInstanceId(), msg.getNodeIp());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("::删除打包文件失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1011204::删除转换文件失败"));
        }
        bus.send(replyMsg);

    }
}
