package com.gcloud.controller.compute.workflow.model.storage;

import com.gcloud.header.storage.model.VmVolumeDetail;

public class ConfigDataDiskFileFlowCommandReq {

	private String instanceId;
	private String vmHostName;
	private VmVolumeDetail volumeDetail;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public VmVolumeDetail getVolumeDetail() {
		return volumeDetail;
	}

	public void setVolumeDetail(VmVolumeDetail volumeDetail) {
		this.volumeDetail = volumeDetail;
	}

	public String getVmHostName() {
		return vmHostName;
	}

	public void setVmHostName(String vmHostName) {
		this.vmHostName = vmHostName;
	}

}
