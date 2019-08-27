package com.gcloud.header.storage.msg.api.pool;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiAssociateDiskCategoryZoneMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "磁盘类型ID", require = true)
	@NotBlank(message = "::磁盘类型ID不能为空")
	private String diskCategoryId;
	
	@ApiModel(description = "可用区ID", require = true)
	@NotBlank(message = "::可用区ID不能为空")
	private String zoneId;

	public String getDiskCategoryId() {
		return diskCategoryId;
	}
	public void setDiskCategoryId(String diskCategoryId) {
		this.diskCategoryId = diskCategoryId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
