package com.gcloud.controller.storage.workflow.model.volume;

public class DeleteSnapshotFlowCommandReq {
	private String repeatParams;
	private boolean delete=true;
	public String getRepeatParams() {
		return repeatParams;
	}
	public void setRepeatParams(String repeatParams) {
		this.repeatParams = repeatParams;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
}
