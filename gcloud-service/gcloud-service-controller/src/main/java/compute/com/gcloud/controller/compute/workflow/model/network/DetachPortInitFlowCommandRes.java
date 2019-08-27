package com.gcloud.controller.compute.workflow.model.network;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

/**
 * Created by yaowj on 2018/11/15.
 */
public class DetachPortInitFlowCommandRes extends WorkflowFirstStepResException{

    private String taskId;
    private String instanceId;
    private VmNetworkDetail networkDetail;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public VmNetworkDetail getNetworkDetail() {
        return networkDetail;
    }

    public void setNetworkDetail(VmNetworkDetail networkDetail) {
        this.networkDetail = networkDetail;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

}
