package com.gcloud.controller.compute.workflow.model.vm;

public class ConnectDiskFlowCommandReq {
	private String createHost;
	private String volumeId;
	private String diskCategory;//RBD、LVM
	private String storageType;

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

	public String getDiskCategory() {
		return diskCategory;
	}

	public void setDiskCategory(String diskCategory) {
		this.diskCategory = diskCategory;
	}

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
