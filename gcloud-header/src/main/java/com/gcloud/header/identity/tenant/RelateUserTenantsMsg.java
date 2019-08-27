package com.gcloud.header.identity.tenant;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class RelateUserTenantsMsg extends ApiMessage{
	@ApiModel(description = "用户ID", require = true)
	@NotBlank(message = "2010501")
	private String userId;
	@ApiModel(description = "租户ID列表")
	private List<String> tenantIds;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}
}
