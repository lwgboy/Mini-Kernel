package com.gcloud.controller.compute.workflow.model.vm;

public class ModifyVmPasswordVmFlowCommandReq {
	private String instanceId;
	private String hostName;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
