package com.gcloud.header.identity.tenant;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class RelateTenantUsersMsg  extends ApiMessage{
	@ApiModel(description = "租户ID", require = true)
	@NotBlank(message = "2010401")
	private String tenantId;
	@ApiModel(description = "用户ID列表")
	private List<String> userIds;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
}
