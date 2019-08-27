package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.model.ClusterCreateDcInfo;
import com.gcloud.controller.security.model.ClusterCreateVmInfo;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;

public class CreateClusterComponentObjectInfo {

    private String componentId;
    private SecurityComponent component;
    private CreateClusterInfoParams createInfo;
    private ClusterCreateVmInfo createVm;
    private ClusterCreateDcInfo createDc;
    private SecurityClusterComponentObjectType objectType;

    public CreateClusterComponentObjectInfo() {
    }

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

    public CreateClusterInfoParams getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateClusterInfoParams createInfo) {
        this.createInfo = createInfo;
    }
}


