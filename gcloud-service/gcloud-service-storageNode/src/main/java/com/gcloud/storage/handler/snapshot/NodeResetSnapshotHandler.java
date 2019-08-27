
package com.gcloud.storage.handler.snapshot;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.volume.NodeResetSnapshotMsg;
import com.gcloud.header.storage.msg.node.volume.NodeResetSnapshotReplyMsg;
import com.gcloud.storage.service.impl.NodeSnapshotServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeResetSnapshotHandler extends AsyncMessageHandler<NodeResetSnapshotMsg> {

    @Autowired
    private NodeSnapshotServiceImpl snapshotService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeResetSnapshotMsg msg) {
        NodeResetSnapshotReplyMsg replyMsg = msg.deriveMsg(NodeResetSnapshotReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setSnapshotId(msg.getSnapshotId());
        try {
            log.info("正在reset快照：{}, volume {}, snapshot {}", msg.getStorageType(), msg.getVolumeRefId(), msg.getSnapshotRefId());
            replyMsg.setSnapshotsToDelete(this.snapshotService.resetSnapshot(msg.getStorageType(), msg.getPoolName(), msg.getDriverName(), msg.getVolumeRefId(),
                    msg.getSnapshotId(), msg.getSnapshotRefId(), msg.getSize()));
            replyMsg.setSuccess(true);
            log.info("reset快照成功：{}, {}", msg.getStorageType(), msg.getSnapshotRefId());
        }
        catch (Exception ex) {
            log.error("::reset快照失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::reset快照失败"));
        }
        this.bus.send(replyMsg);
    }

}
