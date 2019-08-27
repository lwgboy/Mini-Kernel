
package com.gcloud.controller.storage.handler.node.pool;

import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.pool.NodeCreateStoragePoolReplyMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class NodeCreateStoragePoolReplyHandler extends AsyncMessageHandler<NodeCreateStoragePoolReplyMsg> {

    @Autowired
    private StoragePoolDao poolDao;

    @Override
    public void handle(NodeCreateStoragePoolReplyMsg msg) {
        if (msg.getSuccess()) {
            log.info("创建存储池成功：{}", msg.getPoolId());
        }
        else {
            log.info("创建存储池失败：{} -> {}", msg.getPoolId(), msg.getErrorCode());
            poolDao.deleteById(msg.getPoolId());
        }
    }

}
