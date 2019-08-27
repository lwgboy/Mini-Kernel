
package com.gcloud.storage.handler.pool;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.pool.NodeCreateStoragePoolMsg;
import com.gcloud.header.storage.msg.node.pool.NodeCreateStoragePoolReplyMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeCreateStoragePoolHandler extends AsyncMessageHandler<NodeCreateStoragePoolMsg> {

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeCreateStoragePoolMsg msg) {
        NodeCreateStoragePoolReplyMsg replyMsg = msg.deriveMsg(NodeCreateStoragePoolReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setPoolId(msg.getPoolId());
        try {
            replyMsg.setSuccess(true);
            log.info("创建存储池成功：{}, {}", msg.getStorageType(), msg.getPoolName());
        }
        catch (Exception ex) {
            log.error("::创建存储池失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::创建存储池失败"));
        }
        this.bus.send(replyMsg);
    }
}
