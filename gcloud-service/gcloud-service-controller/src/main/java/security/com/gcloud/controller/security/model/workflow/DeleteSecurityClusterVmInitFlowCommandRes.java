package com.gcloud.controller.security.model.workflow;

public class DeleteSecurityClusterVmInitFlowCommandRes {

    private String componentId;
    private String instanceId;
    private Boolean deleteInTask;
    private Boolean deleteNotExist;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Boolean getDeleteInTask() {
        return deleteInTask;
    }

    public void setDeleteInTask(Boolean deleteInTask) {
        this.deleteInTask = deleteInTask;
    }

    public Boolean getDeleteNotExist() {
        return deleteNotExist;
    }

    public void setDeleteNotExist(Boolean deleteNotExist) {
        this.deleteNotExist = deleteNotExist;
    }
}
