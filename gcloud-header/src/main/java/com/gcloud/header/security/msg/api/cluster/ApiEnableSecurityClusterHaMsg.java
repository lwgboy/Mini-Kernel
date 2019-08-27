package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateNetworkParams;

public class ApiEnableSecurityClusterHaMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiEnableSecurityClusterHaReplyMsg.class;
    }

    private String id;
    private CreateClusterInfoParams createInfo;
    private CreateNetworkParams net;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateClusterInfoParams getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateClusterInfoParams createInfo) {
        this.createInfo = createInfo;
    }

    public CreateNetworkParams getNet() {
        return net;
    }

    public void setNet(CreateNetworkParams net) {
        this.net = net;
    }
}
