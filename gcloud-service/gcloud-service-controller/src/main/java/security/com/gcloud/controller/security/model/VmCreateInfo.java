package com.gcloud.controller.security.model;

import com.gcloud.controller.compute.entity.InstanceType;

public class VmCreateInfo {

    private InstanceType instanceType;

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }
}
