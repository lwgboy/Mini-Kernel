package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiCreateDiskReplyMsg extends ApiReplyMessage {


    private static final long serialVersionUID = 1L;

    @ApiModel(description = "磁盘ID")
    private String diskId;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
