package com.gcloud.controller.security.model;


import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.enums.SecurityComponent;

/**
 * Created by yaowj on 2018/7/31.
 */
public class ComputeClusterCreateObjectInfo {

    private String componentId;
    private SecurityComponent component;
    private ClusterCreateVmInfo createVm;
    private ClusterCreateDcInfo createDc;
    private SecurityClusterComponentObjectType objectType;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public SecurityComponent getComponent() {
        return component;
    }

    public void setComponent(SecurityComponent component) {
        this.component = component;
    }

    public ClusterCreateVmInfo getCreateVm() {
        return createVm;
    }

    public void setCreateVm(ClusterCreateVmInfo createVm) {
        this.createVm = createVm;
    }

    public ClusterCreateDcInfo getCreateDc() {
        return createDc;
    }

    public void setCreateDc(ClusterCreateDcInfo createDc) {
        this.createDc = createDc;
    }

    public SecurityClusterComponentObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(SecurityClusterComponentObjectType objectType) {
        this.objectType = objectType;
    }
}
