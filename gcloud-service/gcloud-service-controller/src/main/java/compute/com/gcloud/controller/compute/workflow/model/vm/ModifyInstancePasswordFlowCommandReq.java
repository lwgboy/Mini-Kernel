package com.gcloud.controller.compute.workflow.model.vm;

public class ModifyInstancePasswordFlowCommandReq {
	private String instanceId;
	private String password;
	private String createHost;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateHost() {
		return createHost;
	}
	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}
	
}
