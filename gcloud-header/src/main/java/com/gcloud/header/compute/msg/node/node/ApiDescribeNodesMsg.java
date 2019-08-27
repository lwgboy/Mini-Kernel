package com.gcloud.header.compute.msg.node.node;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeNodesMsg extends ApiPageMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDescribeNodesReplyMsg.class;
	}
	
	@ApiModel(description = "组ID", require = true)
	@NotBlank(message = "1020101::组ID不能为空")
	private String groupId;

	@ApiModel(description = "关键字", require = false)
	private String key;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
