package com.gcloud.header.security.msg.api.cluster;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;

public class ApiSecurityClusterAddInstanceMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@NotBlank
	private String id;
	@NotEmpty
	private List<String> instanceIds;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getInstanceIds() {
		return instanceIds;
	}
	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

}
