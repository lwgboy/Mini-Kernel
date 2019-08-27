package com.gcloud.controller.compute.workflow.model.trash;

/**
 * Created by yaowj on 2018/12/4.
 */
public class DetachAndDeleteNetcardInfo {

    private String netcardId;
    private boolean delete = true;

    public DetachAndDeleteNetcardInfo(String netcardId, boolean delete) {
        this.netcardId = netcardId;
        this.delete = delete;
    }

    public DetachAndDeleteNetcardInfo() {
    }

    public String getNetcardId() {
        return netcardId;
    }

    public void setNetcardId(String netcardId) {
        this.netcardId = netcardId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
