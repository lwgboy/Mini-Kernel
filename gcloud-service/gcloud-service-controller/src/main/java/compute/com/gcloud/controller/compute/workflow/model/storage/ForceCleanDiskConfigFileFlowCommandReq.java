package com.gcloud.controller.compute.workflow.model.storage;

public class ForceCleanDiskConfigFileFlowCommandReq {

	private String instanceId;
	private String volumeId;
	private String node;

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

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
}
