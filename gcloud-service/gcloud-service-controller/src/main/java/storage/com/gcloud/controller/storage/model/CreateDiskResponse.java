package com.gcloud.controller.storage.model;

import com.gcloud.header.log.model.LongTaskResponse;

/**
 * Created by yaowj on 2018/9/21.
 */
public class CreateDiskResponse extends LongTaskResponse {

    private String diskId;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
