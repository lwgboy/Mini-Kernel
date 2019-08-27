package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

public class CreateSystemDiskFlowCommandReq {
	private DiskInfo dataDisk;
	private String createHost;
	private CurrentUser createUser;

	public DiskInfo getDataDisk() {
		return dataDisk;
	}

	public void setDataDisk(DiskInfo dataDisk) {
		this.dataDisk = dataDisk;
	}

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}
}
