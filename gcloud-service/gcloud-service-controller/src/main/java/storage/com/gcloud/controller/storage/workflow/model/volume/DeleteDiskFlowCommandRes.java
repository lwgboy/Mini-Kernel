package com.gcloud.controller.storage.workflow.model.volume;

/**
 * Created by yaowj on 2018/12/4.
 */
public class DeleteDiskFlowCommandRes {

    private String volumeId;
    private boolean delete = true;

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
