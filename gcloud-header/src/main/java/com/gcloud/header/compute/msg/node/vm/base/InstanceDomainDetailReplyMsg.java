package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.ReplyMessage;

/**
 * Created by yaowj on 2018/11/12.
 */
public class InstanceDomainDetailReplyMsg extends ReplyMessage {

    private Integer memory; // mb
    private Integer currentMemory;
    private Integer vcpu;

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getCurrentMemory() {
        return currentMemory;
    }

    public void setCurrentMemory(Integer currentMemory) {
        this.currentMemory = currentMemory;
    }

    public Integer getVcpu() {
        return vcpu;
    }

    public void setVcpu(Integer vcpu) {
        this.vcpu = vcpu;
    }
}
