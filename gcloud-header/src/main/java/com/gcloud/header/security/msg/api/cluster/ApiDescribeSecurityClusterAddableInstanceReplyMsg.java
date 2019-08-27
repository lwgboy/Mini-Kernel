package com.gcloud.header.security.msg.api.cluster;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.security.model.DescribeSecurityClusterAddableInstanceResponse;
import com.gcloud.header.security.model.SecurityClusterInstanceType;

public class ApiDescribeSecurityClusterAddableInstanceReplyMsg extends PageReplyMessage<SecurityClusterInstanceType>{

	private static final long serialVersionUID = 1L;
	
	
	private DescribeSecurityClusterAddableInstanceResponse securityClusterAddableInstances;

	@Override
	public void setList(List<SecurityClusterInstanceType> list) {
		securityClusterAddableInstances = new DescribeSecurityClusterAddableInstanceResponse();
		securityClusterAddableInstances.setSecurityClusterAddableInstance(list);
	}
	
	public DescribeSecurityClusterAddableInstanceResponse getSecurityClusterAddableInstances() {
		return securityClusterAddableInstances;
	}
	
	public void setSecurityClusterAddableInstances(
			DescribeSecurityClusterAddableInstanceResponse securityClusterAddableInstances) {
		this.securityClusterAddableInstances = securityClusterAddableInstances;
	}
}
