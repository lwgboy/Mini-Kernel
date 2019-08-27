package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiResizeDiskMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }
    @ApiModel(description = "磁盘ID")
    @NotBlank(message = StorageErrorCodes.INPUT_DISK_ID_ERROR)
    private String diskId;
    @ApiModel(description = "磁盘大小。单位:GB")
    @NotNull(message = StorageErrorCodes.INPUT_DISK_SIZE_ERROR)
    private Integer newSize;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public Integer getNewSize() {
        return newSize;
    }

    public void setNewSize(Integer newSize) {
        this.newSize = newSize;
    }
}
