package com.gcloud.controller.network.handler.node.bridge;

import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.network.msg.node.bridge.DeleteOvsBridgeReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class DeleteOvsBridgeReplyHandler extends AsyncMessageHandler<DeleteOvsBridgeReplyMsg> {

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Override
    public void handle(DeleteOvsBridgeReplyMsg msg) {

        if(msg.getSuccess()){
            ovsBridgeDao.deleteById(msg.getId());
        }

    }
}
