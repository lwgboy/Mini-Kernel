package com.gcloud.header.security.msg.api.cluster;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.security.model.ApiListSecurityClusterResponse;
import com.gcloud.header.security.model.SecurityClusterListType;

public class ApiListSecurityCluseterReplyMsg extends PageReplyMessage<SecurityClusterListType>{
	private static final long serialVersionUID = 1L;
	
	private ApiListSecurityClusterResponse securityClusterLists;
	
	@Override
	public void setList(List<SecurityClusterListType> list) {
		securityClusterLists = new ApiListSecurityClusterResponse();
		securityClusterLists.setSecurityClusterList(list);
	}

	public ApiListSecurityClusterResponse getSecurityClusterLists() {
		return securityClusterLists;
	}

	public void setSecurityClusterLists(ApiListSecurityClusterResponse securityClusterLists) {
		this.securityClusterLists = securityClusterLists;
	}
}
