package com.gcloud.header.compute.msg.api.trash;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/12/3.
 */
public class ApiDeleteInstanceMsg extends ApiMessage {

    @ApiModel(description = "云服务器ID", require = true)
    @NotBlank(message = "0010801::云服务器ID不能为空")
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }
}
