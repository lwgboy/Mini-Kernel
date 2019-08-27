package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

import org.hibernate.validator.constraints.NotBlank;

public class DeleteNetworkInterfaceMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @ApiModel(description = "网卡ID", require = true)
    @NotBlank(message = "0080301::端口ID不能为空")
    private String networkInterfaceId;

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public void setNetworkInterfaceId(String networkInterfaceId) {
        this.networkInterfaceId = networkInterfaceId;
    }

    @Override
    public Class replyClazz() {
        // TODO Auto-generated method stub
        return ApiReplyMessage.class;
    }

}
