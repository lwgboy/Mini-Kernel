package com.gcloud.header.slb.msg.api;

import java.util.ArrayList;
import java.util.List;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.slb.model.VServerGroupSetType;

public class ApiDescribeVServerGroupsReplyMsg extends ApiReplyMessage{
	
	@ApiModel(description = "服务器组列表")
	private List<VServerGroupSetType> VServerGroups=new ArrayList<>();

	public List<VServerGroupSetType> getVServerGroups() {
		return VServerGroups;
	}

	public void setVServerGroups(List<VServerGroupSetType> vServerGroups) {
		VServerGroups = vServerGroups;
	}
}
