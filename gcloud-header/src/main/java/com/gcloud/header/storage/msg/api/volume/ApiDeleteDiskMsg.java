package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiDeleteDiskMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    @ApiModel(description = "磁盘ID", require = true)
    @NotBlank(message = StorageErrorCodes.INPUT_DISK_ID_ERROR)
    private String diskId;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
