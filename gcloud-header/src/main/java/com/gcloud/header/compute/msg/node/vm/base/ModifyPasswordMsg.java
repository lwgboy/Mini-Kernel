package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

public class ModifyPasswordMsg extends NodeMessage {
	private static final long serialVersionUID = 1L;

	private String instanceId;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
