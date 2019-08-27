package com.gcloud.header.storage.msg.api.snapshot;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.storage.model.DescribeDisksResponse;
import com.gcloud.header.storage.model.DescribeSnapshotsResponse;
import com.gcloud.header.storage.model.DiskItemType;
import com.gcloud.header.storage.model.SnapshotType;

import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class ApiDescribeSnapshotsReplyMsg extends PageReplyMessage<SnapshotType> {

    private static final long serialVersionUID = 1L;

    private DescribeSnapshotsResponse snapshots;

    @Override
    public void setList(List<SnapshotType> list) {
        snapshots = new DescribeSnapshotsResponse();
        snapshots.setSnapshot(list);
    }

    public DescribeSnapshotsResponse getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(DescribeSnapshotsResponse snapshots) {
        this.snapshots = snapshots;
    }
}
