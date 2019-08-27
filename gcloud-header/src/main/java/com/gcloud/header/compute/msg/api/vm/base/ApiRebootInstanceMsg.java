package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/17.
 */
public class ApiRebootInstanceMsg extends ApiMessage {
	@ApiModel(description="云服务器ID", require=true)
    @NotBlank(message = "0010401::云服务器ID不能为空")
    private String instanceId;
	@ApiModel(description="是否强强制执行",type="boolean")
    private Boolean forceStop;

    @Override
    public Class replyClazz() {
        return ApiRebootInstanceReplyMsg.class;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getForceStop() {
        return forceStop;
    }

    public void setForceStop(Boolean forceStop) {
        this.forceStop = forceStop;
    }

}
