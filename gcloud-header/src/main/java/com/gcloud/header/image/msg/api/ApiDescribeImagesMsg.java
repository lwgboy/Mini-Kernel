package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiDescribeImagesMsg extends ApiPageMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDescribeImagesReplyMsg.class;
    }

    @ApiModel(description = "镜像 ID", require = false)
    private String imageId;
    
    @ApiModel(description = "镜像名称", require = false)
    private String imageName;
    
    @ApiModel(description = "镜像状态", require = false)
    private String status;
    
    @ApiModel(description = "是否禁用", require = false)
    private Boolean disable;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
}
