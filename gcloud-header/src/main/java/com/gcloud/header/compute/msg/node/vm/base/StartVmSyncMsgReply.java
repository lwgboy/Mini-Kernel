package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.ReplyMessage;

/**
 * Created by yaowj on 2018/9/11.
 */
public class StartVmSyncMsgReply extends ReplyMessage {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
