package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/11/27.
 */
public class DeleteInstanceInitFlowCommandReq {

    private String instanceId;
    private Boolean deleteDataDisk;
    private Boolean deleteNetcard;
    private Boolean inTask = true;  //默认是true

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getDeleteDataDisk() {
        return deleteDataDisk;
    }

    public void setDeleteDataDisk(Boolean deleteDataDisk) {
        this.deleteDataDisk = deleteDataDisk;
    }

    public Boolean getDeleteNetcard() {
        return deleteNetcard;
    }

    public void setDeleteNetcard(Boolean deleteNetcard) {
        this.deleteNetcard = deleteNetcard;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }
}
