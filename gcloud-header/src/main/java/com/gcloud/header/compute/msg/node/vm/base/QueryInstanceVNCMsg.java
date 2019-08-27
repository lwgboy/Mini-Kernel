package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NeedReplyMessage;
import com.gcloud.header.NodeMessage;

public class QueryInstanceVNCMsg extends NeedReplyMessage {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Class replyClazz() {
        return QueryInstanceVNCMsgReply.class;
    }
}
