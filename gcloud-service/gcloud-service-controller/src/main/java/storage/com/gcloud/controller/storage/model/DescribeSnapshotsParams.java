package com.gcloud.controller.storage.model;

import com.gcloud.common.model.PageParams;

import java.util.List;

/**
 * Created by yaowj on 2018/9/28.
 */
public class DescribeSnapshotsParams extends PageParams {

    private String diskId;
    private String snapshotIds;
    private List<String> snapshotIdList;

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

	public List<String> getSnapshotIdList() {
		return snapshotIdList;
	}

	public void setSnapshotIdList(List<String> snapshotIdList) {
		this.snapshotIdList = snapshotIdList;
	}
}
