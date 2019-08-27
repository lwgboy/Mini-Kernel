package com.gcloud.controller.compute.workflow.model.vm;

public class CreateInstanceFlowDoneCommandReq {
	private String instanceId;
	//创建默认清除任务状态
	private Boolean inTask = false;

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
