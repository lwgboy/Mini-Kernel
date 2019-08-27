package com.gcloud.header.image.msg.api;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDisableImageMsg extends ApiMessage{
	private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    @NotBlank(message = "0090401::镜像ID不能为空")
    @ApiModel(description = "镜像ID", require = true)
    private String imageId;
    
    @NotNull(message = "0090402::是否禁用不能为空")
    @ApiModel(description = "是否禁用", require = true)
    private boolean disable;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}
    
}
