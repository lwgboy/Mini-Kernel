package com.gcloud.header.compute.msg.node.vm.senior;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.storage.model.VmVolumeDetail;

/**
 * Created by yaowj on 2018/11/30.
 */
public class ConvertInstanceMsg extends NodeMessage {

    private String instanceId;
    private String targetFormat;
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

    public VmVolumeDetail getVolumeDetail() {
        return volumeDetail;
    }

    public void setVolumeDetail(VmVolumeDetail volumeDetail) {
        this.volumeDetail = volumeDetail;
    }
}
