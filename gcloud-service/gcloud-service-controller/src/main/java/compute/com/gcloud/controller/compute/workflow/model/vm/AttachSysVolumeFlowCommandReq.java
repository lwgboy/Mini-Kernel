package com.gcloud.controller.compute.workflow.model.vm;

public class AttachSysVolumeFlowCommandReq {
	private String volumeId;
	private String instanceUuid;
	private String hostName;
	
	private String userId;
	private String instanceId;
	
	private String targetDev = "vda";
	private String busType = "virtio";
	
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getTargetDev() {
		return targetDev;
	}
	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	
}
