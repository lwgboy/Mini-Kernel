package com.gcloud.controller.compute.workflow.model.storage;

public class ModifyInstanceSpecWorkflowReq {

	private String instanceId;
	private String instanceType;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

}
