package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NeedReplyMessage;

/**
 * Created by yaowj on 2018/9/11.
 */
public class InstanceDomainDetailMsg extends NeedReplyMessage {

    @Override
    public Class replyClazz() {
        return InstanceDomainDetailReplyMsg.class;
    }

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

}
