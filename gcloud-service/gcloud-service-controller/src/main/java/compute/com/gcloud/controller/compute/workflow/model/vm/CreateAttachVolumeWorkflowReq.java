package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.compute.msg.api.model.DiskInfo;

public class CreateAttachVolumeWorkflowReq {
	private DiskInfo repeatParams;
	private String hostName;
	private String instanceId;

	public DiskInfo getRepeatParams() {
		return repeatParams;
	}

	public void setRepeatParams(DiskInfo repeatParams) {
		this.repeatParams = repeatParams;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
