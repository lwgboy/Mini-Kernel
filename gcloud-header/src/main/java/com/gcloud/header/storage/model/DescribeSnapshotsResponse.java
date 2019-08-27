package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class DescribeSnapshotsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SnapshotType> snapshot;

    public List<SnapshotType> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(List<SnapshotType> snapshot) {
        this.snapshot = snapshot;
    }
}
