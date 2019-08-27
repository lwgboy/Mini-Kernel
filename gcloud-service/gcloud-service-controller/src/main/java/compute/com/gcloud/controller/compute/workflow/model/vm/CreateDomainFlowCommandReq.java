package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.api.model.CurrentUser;

public class CreateDomainFlowCommandReq {

	private String createHost;
	private String instanceId;
	private CurrentUser createUser;

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}
}
