package com.gcloud.header.storage.msg.api.pool;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDetailDiskCategoryMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiDetailDiskCategoryReplyMsg.class;
	}
	
	@ApiModel(description = "磁盘类型ID", require = true)
	@NotBlank(message = "::磁盘类型ID不能为空")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
