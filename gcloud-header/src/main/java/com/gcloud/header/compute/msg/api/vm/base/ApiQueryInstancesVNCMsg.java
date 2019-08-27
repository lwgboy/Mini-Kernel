package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiQueryInstancesVNCMsg extends ApiMessage {
    @ApiModel(description="实例ID")
    private String instanceId;

    @Override
    public Class replyClazz() {
        return ApiQueryInstancesVNCReplyMsg.class;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
