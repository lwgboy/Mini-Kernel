package com.gcloud.controller.security.model;

import com.gcloud.controller.security.model.workflow.CreateClusterComponentObjectInfo;
import com.gcloud.controller.security.model.workflow.SecurityClusterComponentHaNetcardInfo;

import java.util.List;

/**
 * Created by yaowj on 2018/7/6.
 */
public class EnableClusterHaResponse {

    private String id;
    private List<CreateClusterComponentObjectInfo> components;
    private List<ClusterCreateOvsBridgeInfo> bridgeInfos;
    private List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos;

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

    public List<SecurityClusterComponentHaNetcardInfo> getHaNetcardInfos() {
        return haNetcardInfos;
    }

    public void setHaNetcardInfos(List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos) {
        this.haNetcardInfos = haNetcardInfos;
    }
}
