
package com.gcloud.controller.storage.handler.node.snapshot;

import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.volume.NodeCreateSnapshotReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class NodeCreateSnapshotReplyHandler extends AsyncMessageHandler<NodeCreateSnapshotReplyMsg> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public void handle(NodeCreateSnapshotReplyMsg msg) {
        if (msg.getSuccess()) {
            this.snapshotService.handleCreateSnapshotSuccess(msg.getSnapshotId());
        }
        else {
            this.snapshotService.handleCreateSnapshotFailed(msg.getErrorCode(), msg.getSnapshotId());
        }
    }

}
