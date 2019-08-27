package com.gcloud.controller.compute.workflow.model.vm;

import java.util.List;

import com.gcloud.header.storage.model.DiskConnection;

public class AttachVolumeFlowCommandReq {
	private String instanceId;
	private String hostName;
	private String volumeId;
	private String targetDev;
	private Integer size;
	private String category;
	private List<DiskConnection> connections;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<DiskConnection> getConnections() {
		return connections;
	}

	public void setConnections(List<DiskConnection> connections) {
		this.connections = connections;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getTargetDev() {
		return targetDev;
	}

	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

}
