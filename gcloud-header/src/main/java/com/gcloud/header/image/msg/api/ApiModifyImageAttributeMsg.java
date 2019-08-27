package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiModifyImageAttributeMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    @NotBlank(message = "0090201::镜像ID不能为空")
    @ApiModel(description = "镜像ID")
    private String imageId;
    @Length(max = 255, message = "0090203::名称长度不能大于255")
    @ApiModel(description = "镜像名称")
    private String imageName;

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

}
