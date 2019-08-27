package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.Module;

import java.util.List;

public class ApiDemoStartVmsMsg extends ApiMessage {
	private List<String> instanceIds;
	private String gTaskId;

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiDemoStartVmsMsgReply.class;
	}

	public String getgTaskId() {
		return gTaskId;
	}

	public void setgTaskId(String gTaskId) {
		this.gTaskId = gTaskId;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}
}
