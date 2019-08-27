package com.gcloud.header.identity.tenant;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class UpdateTenantMsg extends ApiMessage{
	@ApiModel(description = "租户ID", require = true)
	@NotBlank(message = "2010201")
	private String id;
	@ApiModel(description = "租户名", require = true)
	@NotBlank(message = "2010202")
	private String name;
	@ApiModel(description = "备注")
	@Length(max=255, message = "2010203")
	private String description;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

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
