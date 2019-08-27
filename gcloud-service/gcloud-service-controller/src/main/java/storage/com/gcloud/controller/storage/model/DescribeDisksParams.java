package com.gcloud.controller.storage.model;

import java.util.List;

import com.gcloud.common.model.PageParams;

/**
 * Created by yaowj on 2018/9/28.
 */
public class DescribeDisksParams extends PageParams {

    private String diskType;
    private String status;
    private String diskName;
    private String instanceId;
    private List<String> diskIds;

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

	public List<String> getDiskIds() {
		return diskIds;
	}

	public void setDiskIds(List<String> diskIds) {
		this.diskIds = diskIds;
	}
}
