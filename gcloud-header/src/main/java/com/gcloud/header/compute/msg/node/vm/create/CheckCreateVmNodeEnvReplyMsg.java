package com.gcloud.header.compute.msg.node.vm.create;

import com.gcloud.header.NodeMessage;

public class CheckCreateVmNodeEnvReplyMsg  extends NodeMessage {
	private String instancePath;

	public String getInstancePath() {
		return instancePath;
	}

	public void setInstancePath(String instancePath) {
		this.instancePath = instancePath;
	}

}
