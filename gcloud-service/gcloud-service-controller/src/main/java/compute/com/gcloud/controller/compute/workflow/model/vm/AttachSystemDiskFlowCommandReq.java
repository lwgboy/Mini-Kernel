package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.storage.model.DiskConnection;

import java.util.List;

public class AttachSystemDiskFlowCommandReq {
	private String instanceId;
	private String createHost;
	private String volumeId;
	private String targetDev = "vda";
	private Integer size;
	private String category;
	private String storageType;
	private List<DiskConnection> connections;
	
	private String busType = "virtio";

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getTargetDev() {
		return targetDev;
	}

	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

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

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
