package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/3.
 */
public class DeleteInstanceWorkflowReq {

    private String instanceId;
    private Boolean inTask = true;  //默认是true
    private Boolean deleteNotExist = false; //如果不存在，是否执行。 true不执行，false执行。（会抛错找不到云服务器的错误。）

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getInTask() {
        return inTask;
    }

    public void setInTask(Boolean inTask) {
        this.inTask = inTask;
    }

    public Boolean getDeleteNotExist() {
        return deleteNotExist;
    }

    public void setDeleteNotExist(Boolean deleteNotExist) {
        this.deleteNotExist = deleteNotExist;
    }
}
