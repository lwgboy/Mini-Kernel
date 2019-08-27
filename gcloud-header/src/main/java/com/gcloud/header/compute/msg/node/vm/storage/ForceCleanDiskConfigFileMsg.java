package com.gcloud.header.compute.msg.node.vm.storage;

import com.gcloud.header.NodeMessage;

public class ForceCleanDiskConfigFileMsg extends NodeMessage {

    private String instanceId;
    private String volumeId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }


}
