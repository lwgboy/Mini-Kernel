package com.gcloud.controller.compute.workflow.model.vm;

public class CreateNetcardFlowCommandReq {
	private String instanceUuid;
	private String securityGroupId;
	private String vSwitchId;
	private String privateIpAddress;
	private String hostName;
	
	private String instanceId;

	public String getInstanceUuid() {
		return instanceUuid;
	}

	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}

	public String getPrivateIpAddress() {
		return privateIpAddress;
	}

	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
}
