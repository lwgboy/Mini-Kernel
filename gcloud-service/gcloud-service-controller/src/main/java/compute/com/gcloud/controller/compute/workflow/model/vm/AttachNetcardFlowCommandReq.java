package com.gcloud.controller.compute.workflow.model.vm;

public class AttachNetcardFlowCommandReq {
	private String instanceId;
	private String portId;
	private String instanceUuid;
	private String hostName;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getPortId() {
		return portId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public String getInstanceUuid() {
		return instanceUuid;
	}
	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
