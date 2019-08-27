
package com.gcloud.controller.storage.handler.node.snapshot;

import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteSnapshotReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class NodeDeleteSnapshotReplyHandler extends AsyncMessageHandler<NodeDeleteSnapshotReplyMsg> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public void handle(NodeDeleteSnapshotReplyMsg msg) {
        if (msg.getSuccess()) {
            this.snapshotService.handleDeleteSnapshotSuccess(msg.getSnapshotId());
        }
        else {
            this.snapshotService.handleDeleteSnapshotFailed(msg.getErrorCode(), msg.getSnapshotId());
        }
    }

}
