package com.gcloud.header.identity.user;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class GetUserMsg extends ApiMessage{
	@ApiModel(description = "用户ID", require = true)
	@NotBlank(message="2010401")
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Class replyClazz() {
		return GetUserReplyMsg.class;
	}

}
