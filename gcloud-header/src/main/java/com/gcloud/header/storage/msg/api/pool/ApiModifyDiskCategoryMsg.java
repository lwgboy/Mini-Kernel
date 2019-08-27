package com.gcloud.header.storage.msg.api.pool;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiModifyDiskCategoryMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "磁盘类型ID", require = true)
	@NotBlank(message = "::磁盘类型ID不能为空")
	private String id;
	
	@ApiModel(description = "磁盘最小大小", require = true)
	@NotBlank(message = "::磁盘最小大小不能为空")
	private Integer minSize;
	
	@ApiModel(description = "磁盘最大大小", require = true)
	@NotBlank(message = "::磁盘最大大小不能为空")
	private Integer maxSize;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMinSize() {
		return minSize;
	}
	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}
	public Integer getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
}
