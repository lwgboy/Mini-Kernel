package com.gcloud.controller.storage.workflow.model.volume;

import java.util.List;

public class DeleteDiskInitFlowCommandRes {
	List<String> snapshotIds;

	public List<String> getSnapshotIds() {
		return snapshotIds;
	}

	public void setSnapshotIds(List<String> snapshotIds) {
		this.snapshotIds = snapshotIds;
	}
	
}
