package com.gcloud.header.compute.msg.api.vm.create;

import com.gcloud.header.ApiReplyMessage;

public class ApiCreateInstanceReplyMsg extends ApiReplyMessage {

	private String instanceId;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
