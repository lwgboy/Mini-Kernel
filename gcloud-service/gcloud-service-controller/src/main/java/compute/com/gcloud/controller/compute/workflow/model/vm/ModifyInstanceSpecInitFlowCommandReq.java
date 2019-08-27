package com.gcloud.controller.compute.workflow.model.vm;

public class ModifyInstanceSpecInitFlowCommandReq {

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
