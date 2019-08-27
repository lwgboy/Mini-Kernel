
package com.gcloud.controller.storage.handler.node.pool;

import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.pool.NodeReportStoragePoolReplyMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class NodeReportStoragePoolReplyHandler extends AsyncMessageHandler<NodeReportStoragePoolReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public void handle(NodeReportStoragePoolReplyMsg msg) {
        log.info("[ReportStoragePool] received: " + msg);
        try {
            this.poolService.reportStoragePool(msg.getDisplayName(), msg.getProvider(), msg.getStorageType(), msg.getPoolName(), msg.getCategoryCode(), msg.getHostname(),
                    msg.getDriver());
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
