package com.gcloud.header.identity.tenant;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class CreateTenantMsg extends ApiMessage{
	@ApiModel(description = "租户名", require = true)
	@NotBlank(message = "2010101")
	private String name;
	@ApiModel(description = "备注")
	@Length(max=255, message = "2010102")
	private String description;
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
	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
}
