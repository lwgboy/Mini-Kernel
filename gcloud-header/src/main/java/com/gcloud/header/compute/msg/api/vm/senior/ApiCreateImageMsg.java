package com.gcloud.header.compute.msg.api.vm.senior;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/17.
 */
public class ApiCreateImageMsg extends ApiMessage {

    @NotBlank(message = "0011201::云服务器ID不能为空")
    @ApiModel(description = "云服务器ID")
    private String instanceId;
    @Length(min=4, max=255, message="0011204::镜像名称长度为4-255")
    @ApiModel(description = "镜像名称")
    private String imageName;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }
}
