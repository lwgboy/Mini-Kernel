package com.gcloud.controller.security.model.workflow;

import java.util.List;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

public class DisableSecurityClusterHaInitFlowCommandRes extends WorkflowFirstStepResException{

    private String taskId;
    private String clusterId;
    private List<SecurityClusterComponentInfo> components;
    private List<SecurityClusterOvsBridgeInfo> ovsBridges;
    private List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos;

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

    public List<SecurityClusterComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(List<SecurityClusterComponentInfo> components) {
        this.components = components;
    }

    public List<SecurityClusterOvsBridgeInfo> getOvsBridges() {
        return ovsBridges;
    }

    public void setOvsBridges(List<SecurityClusterOvsBridgeInfo> ovsBridges) {
        this.ovsBridges = ovsBridges;
    }

    public List<SecurityClusterComponentHaNetcardInfo> getHaNetcardInfos() {
        return haNetcardInfos;
    }

    public void setHaNetcardInfos(List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos) {
        this.haNetcardInfos = haNetcardInfos;
    }
}
