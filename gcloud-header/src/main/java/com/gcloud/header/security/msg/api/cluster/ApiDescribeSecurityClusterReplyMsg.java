package com.gcloud.header.security.msg.api.cluster;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.security.model.DescribeSecurityClusterResponse;
import com.gcloud.header.security.model.SecurityClusterType;

public class ApiDescribeSecurityClusterReplyMsg extends PageReplyMessage<SecurityClusterType> {
	
	private static final long serialVersionUID = 1L;
	
	private DescribeSecurityClusterResponse securityClusters;

    @Override
    public void setList(List<SecurityClusterType> list) {
    	securityClusters = new DescribeSecurityClusterResponse();
    	securityClusters.setSecurityCluster(list);
    }

	public DescribeSecurityClusterResponse getSecurityClusters() {
		return securityClusters;
	}

	public void setSecurityClusters(DescribeSecurityClusterResponse securityClusters) {
		this.securityClusters = securityClusters;
	}
}
