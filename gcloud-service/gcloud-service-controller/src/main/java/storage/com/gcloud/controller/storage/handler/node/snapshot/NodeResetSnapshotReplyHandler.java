
package com.gcloud.controller.storage.handler.node.snapshot;

import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.volume.NodeResetSnapshotReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class NodeResetSnapshotReplyHandler extends AsyncMessageHandler<NodeResetSnapshotReplyMsg> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public void handle(NodeResetSnapshotReplyMsg msg) {
        if (msg.getSuccess()) {
            this.snapshotService.handleResetSnapshotSuccess(msg.getSnapshotId(), msg.getSnapshotsToDelete());
        }
        else {
            this.snapshotService.handleResetSnapshotFailed(msg.getErrorCode(), msg.getSnapshotId());
        }
    }

}
