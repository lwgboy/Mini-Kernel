package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.storage.model.VmVolumeDetail;

public class VolumeQemuImgFlowCommandRes {
	private VmVolumeDetail disk;
	private String dataDiskPath;
	private int systemQcow2Size;
	public VmVolumeDetail getDisk() {
		return disk;
	}
	public void setDisk(VmVolumeDetail disk) {
		this.disk = disk;
	}
	public String getDataDiskPath() {
		return dataDiskPath;
	}
	public void setDataDiskPath(String dataDiskPath) {
		this.dataDiskPath = dataDiskPath;
	}
	public int getSystemQcow2Size() {
		return systemQcow2Size;
	}
	public void setSystemQcow2Size(int systemQcow2Size) {
		this.systemQcow2Size = systemQcow2Size;
	}
	
}