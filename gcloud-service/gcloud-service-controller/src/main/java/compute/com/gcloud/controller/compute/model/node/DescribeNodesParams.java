package com.gcloud.controller.compute.model.node;

import com.gcloud.common.model.PageParams;

public class DescribeNodesParams extends PageParams{
	private String groupId;
	private String key;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
