package com.gcloud.header.compute.msg.node.vm.create;

import com.gcloud.header.NodeMessage;

public class CreateDomainMsg extends NodeMessage {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String instanceId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
