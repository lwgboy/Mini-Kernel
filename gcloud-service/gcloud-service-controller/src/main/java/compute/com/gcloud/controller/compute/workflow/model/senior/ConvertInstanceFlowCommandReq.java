package com.gcloud.controller.compute.workflow.model.senior;

import com.gcloud.header.storage.model.VmVolumeDetail;

/**
 * Created by yaowj on 2018/11/30.
 */
public class ConvertInstanceFlowCommandReq {

    private String instanceId;
    private String targetFormat;

    //如果不入参，则获取节点的ip和节点的虚拟机信息中的userId
    private String nodeIp;

    //系统盘的详情
    private VmVolumeDetail volumeDetail;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public VmVolumeDetail getVolumeDetail() {
        return volumeDetail;
    }

    public void setVolumeDetail(VmVolumeDetail volumeDetail) {
        this.volumeDetail = volumeDetail;
    }
}
