package com.gcloud.header.security.msg.api.cluster;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;

public class ApiModifySecurityClusterMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@NotBlank
	private String id;
	@NotBlank
	private String name;
	private String description;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
