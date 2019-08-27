package com.gcloud.controller.security.model;

import com.gcloud.controller.security.model.workflow.CreateClusterComponentObjectInfo;

import java.util.List;

/**
 * Created by yaowj on 2018/7/6.
 */
public class CreateClusterResponse {

    private String id;
    private List<CreateClusterComponentObjectInfo> components;
    private List<ClusterCreateOvsBridgeInfo> bridgeInfos;
    private Boolean ha;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CreateClusterComponentObjectInfo> getComponents() {
        return components;
    }

    public void setComponents(List<CreateClusterComponentObjectInfo> components) {
        this.components = components;
    }

    public List<ClusterCreateOvsBridgeInfo> getBridgeInfos() {
        return bridgeInfos;
    }

    public void setBridgeInfos(List<ClusterCreateOvsBridgeInfo> bridgeInfos) {
        this.bridgeInfos = bridgeInfos;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }
}
