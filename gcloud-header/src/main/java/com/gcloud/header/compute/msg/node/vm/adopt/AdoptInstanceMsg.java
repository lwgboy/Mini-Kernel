package com.gcloud.header.compute.msg.node.vm.adopt;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;

import java.util.List;

public class AdoptInstanceMsg extends NodeMessage {

    private List<VmDetail> instances;
    private List<VmDetail> exceptionVms;

    public List<VmDetail> getInstances() {
        return instances;
    }

    public void setInstances(List<VmDetail> instances) {
        this.instances = instances;
    }

    public List<VmDetail> getExceptionVms() {
        return exceptionVms;
    }

    public void setExceptionVms(List<VmDetail> exceptionVms) {
        this.exceptionVms = exceptionVms;
    }
}
