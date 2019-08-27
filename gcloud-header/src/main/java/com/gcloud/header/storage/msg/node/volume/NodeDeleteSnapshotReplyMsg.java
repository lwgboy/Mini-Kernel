
package com.gcloud.header.storage.msg.node.volume;

import com.gcloud.header.NodeMessage;

public class NodeDeleteSnapshotReplyMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String snapshotId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

}
