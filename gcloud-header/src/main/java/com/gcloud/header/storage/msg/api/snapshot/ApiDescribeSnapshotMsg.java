package com.gcloud.header.storage.msg.api.snapshot;

import java.util.List;

import com.gcloud.header.ApiMessage;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiDescribeSnapshotMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDescribeSnapshotsReplyMsg.class;
    }

    private String diskId;
    private String snapshotIds;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

	public String getSnapshotIds() {
		return snapshotIds;
	}

	public void setSnapshotIds(String snapshotIds) {
		this.snapshotIds = snapshotIds;
	}
}
