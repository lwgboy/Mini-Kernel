package com.gcloud.controller.security.model.workflow;

import java.util.List;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

public class DeleteSecurityClusterInitFlowCommandRes extends WorkflowFirstStepResException{

    private String taskId;
    private List<SecurityClusterComponentInfo> components;
    private List<SecurityClusterOvsBridgeInfo> ovsBridges;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
