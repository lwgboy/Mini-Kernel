package com.gcloud.header.storage.msg.api.snapshot;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiCreateSnapshotMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiCreateSnapshotReplyMsg.class;
    }

    @NotBlank(message = StorageErrorCodes.INPUT_DISK_ID_ERROR)
    private String diskId;
    @Length(max = 255, message = StorageErrorCodes.INPUT_SNAPSHOT_NAME_ERROR)
    private String snapshotName;
    @Length(max = 255, message = StorageErrorCodes.INPUT_SNAPSHOT_DESCRIPTION_ERROR)
    private String description;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
