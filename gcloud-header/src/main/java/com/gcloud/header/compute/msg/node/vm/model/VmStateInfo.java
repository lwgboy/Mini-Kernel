package com.gcloud.header.compute.msg.node.vm.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2019/1/10.
 */
public class VmStateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instanceId;
    private String state;
    private String gcState;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGcState() {
        return gcState;
    }

    public void setGcState(String gcState) {
        this.gcState = gcState;
    }
}
