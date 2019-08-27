package com.gcloud.controller.compute.workflow.model.senior;

/**
 * Created by yaowj on 2018/11/30.
 */
public class DeleteBundleFlowCommandReq {

    private String instanceId;

    //如果不入参，则获取节点的ip和节点的虚拟机信息中的userId
    private String nodeIp;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }


}
