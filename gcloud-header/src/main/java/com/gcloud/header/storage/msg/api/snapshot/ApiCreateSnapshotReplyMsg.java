package com.gcloud.header.storage.msg.api.snapshot;

import com.gcloud.header.ApiReplyMessage;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiCreateSnapshotReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private String snapshotId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
}
