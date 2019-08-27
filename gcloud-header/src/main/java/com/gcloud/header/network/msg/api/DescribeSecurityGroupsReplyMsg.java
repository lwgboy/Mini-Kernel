package com.gcloud.header.network.msg.api;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.controller.ControllerProperty;
import com.gcloud.header.network.model.DescribeSecurityGroupResponse;
import com.gcloud.header.network.model.SecurityGroupItemType;

import java.util.List;

public class DescribeSecurityGroupsReplyMsg extends PageReplyMessage<SecurityGroupItemType>{
	private DescribeSecurityGroupResponse securityGroups;
	private String regionId = ControllerProperty.REGION_ID;

	@Override
	public void setList(List<SecurityGroupItemType> list) {
		securityGroups = new DescribeSecurityGroupResponse();
		securityGroups.setSecurityGroup(list);
		
	}
	public DescribeSecurityGroupResponse getSecurityGroups() {
		return securityGroups;
	}
	public void setSecurityGroups(DescribeSecurityGroupResponse securityGroups) {
		this.securityGroups = securityGroups;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
}
