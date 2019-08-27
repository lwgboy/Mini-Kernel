package com.gcloud.header.compute.msg.api.vm.base;


import java.util.List;


import com.gcloud.header.ApiMessage;

public class ApiStopVmsMsg extends ApiMessage{
	private List<String> instanceIds;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiStopVmsReplyMsg.class;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}
	
}
