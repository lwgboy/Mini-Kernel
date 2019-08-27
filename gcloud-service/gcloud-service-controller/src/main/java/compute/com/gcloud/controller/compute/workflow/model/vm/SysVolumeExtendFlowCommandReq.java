package com.gcloud.controller.compute.workflow.model.vm;

public class SysVolumeExtendFlowCommandReq {
	private String instanceUuid;
	private String volumeId;
	private int imageSize;
	private int systemDiskSize;//传入的大小
	private int systemQcow2Size;
	
	public String getInstanceUuid() {
		return instanceUuid;
	}
	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public int getImageSize() {
		return imageSize;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	public int getSystemDiskSize() {
		return systemDiskSize;
	}
	public void setSystemDiskSize(int systemDiskSize) {
		this.systemDiskSize = systemDiskSize;
	}
	public int getSystemQcow2Size() {
		return systemQcow2Size;
	}
	public void setSystemQcow2Size(int systemQcow2Size) {
		this.systemQcow2Size = systemQcow2Size;
	}
}
