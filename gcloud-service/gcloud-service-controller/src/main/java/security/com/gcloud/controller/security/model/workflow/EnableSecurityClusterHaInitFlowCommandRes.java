package com.gcloud.controller.security.model.workflow;

import com.gcloud.controller.security.model.ClusterCreateOvsBridgeInfo;
import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

import java.util.List;

public class EnableSecurityClusterHaInitFlowCommandRes extends WorkflowFirstStepResException{

    private String taskId;
    private String clusterId;
    private List<CreateClusterComponentObjectInfo> components;
    private List<ClusterCreateOvsBridgeInfo> bridgeInfos;
    private List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos;

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

    public List<SecurityClusterComponentHaNetcardInfo> getHaNetcardInfos() {
        return haNetcardInfos;
    }

    public void setHaNetcardInfos(List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos) {
        this.haNetcardInfos = haNetcardInfos;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
