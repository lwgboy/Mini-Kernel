package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiPageMessage;

public class ApiDescribeSecurityClusterMsg extends ApiPageMessage {

	private static final long serialVersionUID = 1L;
	
	@Override
    public Class replyClazz() {
        return ApiDescribeSecurityClusterReplyMsg.class;
    }
    
	private String securityClusterName;

	public String getSecurityClusterName() {
		return securityClusterName;
	}

	public void setSecurityClusterName(String securityClusterName) {
		this.securityClusterName = securityClusterName;
	}
}
