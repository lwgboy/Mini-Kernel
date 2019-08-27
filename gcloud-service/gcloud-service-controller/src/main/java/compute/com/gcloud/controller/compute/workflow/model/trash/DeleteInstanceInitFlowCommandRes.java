package com.gcloud.controller.compute.workflow.model.trash;

import java.util.List;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

/**
 * Created by yaowj on 2018/11/27.
 */
public class DeleteInstanceInitFlowCommandRes extends WorkflowFirstStepResException{

    private String vmUserId;
    private List<DetachAndDeleteDiskInfo> disks;
    private List<DetachAndDeleteNetcardInfo> netcards;

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<DetachAndDeleteDiskInfo> getDisks() {
        return disks;
    }

    public void setDisks(List<DetachAndDeleteDiskInfo> disks) {
        this.disks = disks;
    }

    public List<DetachAndDeleteNetcardInfo> getNetcards() {
        return netcards;
    }

    public void setNetcards(List<DetachAndDeleteNetcardInfo> netcards) {
        this.netcards = netcards;
    }

    public String getVmUserId() {
        return vmUserId;
    }

    public void setVmUserId(String vmUserId) {
        this.vmUserId = vmUserId;
    }
}
