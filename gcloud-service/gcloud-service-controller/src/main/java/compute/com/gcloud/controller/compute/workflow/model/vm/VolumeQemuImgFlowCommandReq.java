package com.gcloud.controller.compute.workflow.model.vm;

public class VolumeQemuImgFlowCommandReq {
	private String hostName;
//	private String diskPath;
//	private int diskQcow2Size;
	private String volumeId;
	private String userId;
	private String instanceId;
	private Integer StorageType;
	
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
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public Integer getStorageType() {
		return StorageType;
	}
	public void setStorageType(Integer storageType) {
		StorageType = storageType;
	}
	
}
