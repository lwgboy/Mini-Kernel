
package com.gcloud.header.storage.msg.node.volume;

import java.util.List;

import com.gcloud.header.NodeMessage;

public class NodeResetSnapshotReplyMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String snapshotId;
    private List<String> snapshotsToDelete;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public List<String> getSnapshotsToDelete() {
        return snapshotsToDelete;
    }

    public void setSnapshotsToDelete(List<String> snapshotsToDelete) {
        this.snapshotsToDelete = snapshotsToDelete;
    }

}
