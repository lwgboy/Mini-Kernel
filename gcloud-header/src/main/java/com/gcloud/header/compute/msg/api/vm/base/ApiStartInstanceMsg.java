package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/17.
 */
public class ApiStartInstanceMsg extends ApiMessage {

    @ApiModel(description = "云服务器ID", require = true)
    @NotBlank(message = "0010201::云服务器ID不能为空")
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public Class replyClazz() {
        return ApiStartInstanceReplyMsg.class;
    }
}
