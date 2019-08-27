package com.gcloud.controller.compute.workflow.model.vm;

public class UpdateHostnamePasswdFlowCommandReq {
	private String instanceId;
	private String hostName;
	private String password;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
