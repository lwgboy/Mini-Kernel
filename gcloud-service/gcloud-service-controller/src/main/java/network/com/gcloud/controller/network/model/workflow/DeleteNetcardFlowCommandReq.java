package com.gcloud.controller.network.model.workflow;

public class DeleteNetcardFlowCommandReq {

    private String netcardId;
    private boolean delete = true;

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
