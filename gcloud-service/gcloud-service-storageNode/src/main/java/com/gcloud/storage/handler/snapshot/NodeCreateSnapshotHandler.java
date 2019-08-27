
package com.gcloud.storage.handler.snapshot;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.volume.NodeCreateSnapshotMsg;
import com.gcloud.header.storage.msg.node.volume.NodeCreateSnapshotReplyMsg;
import com.gcloud.storage.service.impl.NodeSnapshotServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeCreateSnapshotHandler extends AsyncMessageHandler<NodeCreateSnapshotMsg> {

    @Autowired
    private NodeSnapshotServiceImpl snapshotService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeCreateSnapshotMsg msg) {
        NodeCreateSnapshotReplyMsg replyMsg = msg.deriveMsg(NodeCreateSnapshotReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setSnapshotId(msg.getSnapshotId());
        try {
            log.info("正在创建快照：{}, volume {}, snapshot {}", msg.getStorageType(), msg.getVolumeRefId(), msg.getSnapshotRefId());
            this.snapshotService.createSnapshot(msg.getStorageType(), msg.getPoolName(), msg.getDriverName(), msg.getVolumeRefId(), msg.getSnapshotId(), msg.getSnapshotRefId());
            replyMsg.setSuccess(true);
            log.info("创建快照成功：{}, {}", msg.getStorageType(), msg.getSnapshotRefId());
        }
        catch (Exception ex) {
            log.error("::创建快照失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::创建快照失败"));
        }
        this.bus.send(replyMsg);
    }

}
