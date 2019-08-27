package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.storage.model.VmVolumeDetail;

public class AttachVolumeFlowCommandRes {

    private VmVolumeDetail volumeDetail;
    private String attachmentId;

    public VmVolumeDetail getVolumeDetail() {
        return volumeDetail;
    }

    public void setVolumeDetail(VmVolumeDetail volumeDetail) {
        this.volumeDetail = volumeDetail;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
