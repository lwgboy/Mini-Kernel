package com.gcloud.controller.storage.workflow.model.volume;

public class DeleteDiskWorkflowReq {
	private String volumeId;
    private boolean deleteSnapshot = true;
    
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public boolean isDeleteSnapshot() {
		return deleteSnapshot;
	}
	public void setDeleteSnapshot(boolean deleteSnapshot) {
		this.deleteSnapshot = deleteSnapshot;
	}
}
