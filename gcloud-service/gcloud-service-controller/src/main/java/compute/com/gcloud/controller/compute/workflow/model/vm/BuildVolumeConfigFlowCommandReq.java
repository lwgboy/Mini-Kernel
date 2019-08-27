package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.storage.model.VmVolumeDetail;

public class BuildVolumeConfigFlowCommandReq {
	private String hostName;

	private VmVolumeDetail volume;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public VmVolumeDetail getVolume() {
		return volume;
	}

	public void setVolume(VmVolumeDetail volume) {
		this.volume = volume;
	}

}
