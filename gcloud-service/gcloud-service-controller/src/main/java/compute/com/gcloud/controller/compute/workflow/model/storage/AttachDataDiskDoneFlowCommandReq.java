package com.gcloud.controller.compute.workflow.model.storage;

public class AttachDataDiskDoneFlowCommandReq {

	private String instanceId;
	private Boolean inTask = true;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }
}
