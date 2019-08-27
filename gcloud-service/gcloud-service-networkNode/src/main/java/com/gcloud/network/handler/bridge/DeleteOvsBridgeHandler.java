package com.gcloud.network.handler.bridge;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.network.msg.node.bridge.DeleteOvsBridgeMsg;
import com.gcloud.header.network.msg.node.bridge.DeleteOvsBridgeReplyMsg;
import com.gcloud.network.service.bridge.IOvsBridgeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class DeleteOvsBridgeHandler extends AsyncMessageHandler<DeleteOvsBridgeMsg> {

    @Autowired
    private IOvsBridgeNodeService iOvsBridgeNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(DeleteOvsBridgeMsg msg) {

        DeleteOvsBridgeReplyMsg replyMsg = msg.deriveMsg(DeleteOvsBridgeReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            iOvsBridgeNodeService.deleteOvsBridge(msg.getBridge());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("::删除ovs网桥失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::删除ovs网桥失败"));
        }
        bus.send(replyMsg);

    }
}
