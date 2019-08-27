package com.gcloud.header.compute.msg.api.network;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/11/15.
 */
public class ApiAttachNetworkInterfaceMsg extends ApiMessage {

    @ApiModel(description = "云服务器ID", require = true)
    @NotBlank(message = "0010601::云服务器ID不能为空")
    private String instanceId;
    @ApiModel(description = "网卡ID", require = true)
    @NotBlank(message = "0010602::网卡ID不能为空")
    private String networkInterfaceId;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public void setNetworkInterfaceId(String networkInterfaceId) {
        this.networkInterfaceId = networkInterfaceId;
    }
}
