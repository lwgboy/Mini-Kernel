package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/29.
 */
public class ApiModifyDiskAttributeMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @ApiModel(description = "磁盘ID", require = true)
    @NotBlank(message = StorageErrorCodes.INPUT_DISK_ID_ERROR)
    private String diskId;
    @ApiModel(description = "磁盘名称", require = true)
    @Length(min = 1, max = 255, message = StorageErrorCodes.INPUT_DISK_NAME_ERROR)
    private String diskName;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }
}
