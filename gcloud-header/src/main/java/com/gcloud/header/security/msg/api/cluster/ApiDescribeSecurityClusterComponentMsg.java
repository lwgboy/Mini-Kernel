package com.gcloud.header.security.msg.api.cluster;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiPageMessage;

public class ApiDescribeSecurityClusterComponentMsg extends ApiPageMessage {

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDescribeSecurityClusterComponentReplyMsg.class;
	}
	
	@NotBlank
	private String id;
	
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
