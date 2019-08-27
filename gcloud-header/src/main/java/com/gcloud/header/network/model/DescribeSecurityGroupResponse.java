package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class DescribeSecurityGroupResponse  implements Serializable{
	private List<SecurityGroupItemType> securityGroup;

	public List<SecurityGroupItemType> getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(List<SecurityGroupItemType> securityGroup) {
		this.securityGroup = securityGroup;
	}
	
}
