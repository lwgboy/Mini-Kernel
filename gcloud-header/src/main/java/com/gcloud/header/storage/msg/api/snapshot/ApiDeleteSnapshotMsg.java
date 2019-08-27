package com.gcloud.header.storage.msg.api.snapshot;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiDeleteSnapshotMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

    @NotBlank(message = StorageErrorCodes.INPUT_SNAPSHOT_ID_ERROR)
    private String snapshotId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
}
