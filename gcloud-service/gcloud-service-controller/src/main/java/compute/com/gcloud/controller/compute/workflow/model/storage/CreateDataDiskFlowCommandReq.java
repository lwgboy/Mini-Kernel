package com.gcloud.controller.compute.workflow.model.storage;

import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

public class CreateDataDiskFlowCommandReq {
	private DiskInfo dataDisk;
	private CurrentUser createUser;

	public DiskInfo getDataDisk() {
		return dataDisk;
	}

	public void setDataDisk(DiskInfo dataDisk) {
		this.dataDisk = dataDisk;
	}

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}
}
