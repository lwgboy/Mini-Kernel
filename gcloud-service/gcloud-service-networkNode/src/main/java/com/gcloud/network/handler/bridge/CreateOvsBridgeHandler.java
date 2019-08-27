package com.gcloud.network.handler.bridge;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.network.msg.node.bridge.CreateOvsBridgeMsg;
import com.gcloud.header.network.msg.node.bridge.CreateOvsBridgeReplyMsg;
import com.gcloud.network.service.bridge.IOvsBridgeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class CreateOvsBridgeHandler extends AsyncMessageHandler<CreateOvsBridgeMsg> {

    @Autowired
    private IOvsBridgeNodeService iOvsBridgeNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(CreateOvsBridgeMsg msg) {

        CreateOvsBridgeReplyMsg replyMsg = msg.deriveMsg(CreateOvsBridgeReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setId(msg.getId());
        try{
            iOvsBridgeNodeService.createOvsBridge(msg.getBridge());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("::创建ovs网桥失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::创建ovs网桥失败"));
        }
        bus.send(replyMsg);

    }
}
