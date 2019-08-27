package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

public class ModifyVmHostNameMsg extends NodeMessage {
	private static final long serialVersionUID = 1L;

	private String instanceId;
	private String hostName;

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

}
