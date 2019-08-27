package com.gcloud.header.security.msg.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/12/11.
 */
public class CreateClusterObjectParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectType;
    private CreateClusterVmParams vm;
    private CreateClusterDcParams dc;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public CreateClusterVmParams getVm() {
        return vm;
    }

    public void setVm(CreateClusterVmParams vm) {
        this.vm = vm;
    }

    public CreateClusterDcParams getDc() {
        return dc;
    }

    public void setDc(CreateClusterDcParams dc) {
        this.dc = dc;
    }
}
