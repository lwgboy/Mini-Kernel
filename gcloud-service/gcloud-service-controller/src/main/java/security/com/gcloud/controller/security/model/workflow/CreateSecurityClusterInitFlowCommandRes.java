package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateOvsBridgeInfo;
import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

import java.util.List;

public class CreateSecurityClusterInitFlowCommandRes extends WorkflowFirstStepResException{
    private String taskId;
    private String clusterId;
    private List<CreateClusterComponentObjectInfo> components;
    private List<ClusterCreateOvsBridgeInfo> bridgeInfos;
    private Boolean ha;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
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
