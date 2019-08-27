package com.gcloud.header.compute.msg.node.vm.storage;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.storage.model.VmVolumeDetail;

public class ConfigDataDiskFileMsg extends NodeMessage {

	private String instanceId;
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

}
