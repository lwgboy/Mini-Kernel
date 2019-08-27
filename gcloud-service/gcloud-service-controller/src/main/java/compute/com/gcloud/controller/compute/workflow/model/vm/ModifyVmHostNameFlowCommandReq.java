package com.gcloud.controller.compute.workflow.model.vm;

public class ModifyVmHostNameFlowCommandReq {
	private String instanceId;
	private String hostName;
	private String createHost;

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

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

}
