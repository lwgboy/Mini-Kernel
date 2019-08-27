
package com.gcloud.header.storage.msg.node.volume;

import com.gcloud.header.NodeMessage;

public class NodeCreateSnapshotMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String storageType;
    private String poolName;
    private String driverName;
    private String volumeRefId;
    private String snapshotId;
    private String snapshotRefId;

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVolumeRefId() {
        return volumeRefId;
    }

    public void setVolumeRefId(String volumeRefId) {
        this.volumeRefId = volumeRefId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotRefId() {
        return snapshotRefId;
    }

    public void setSnapshotRefId(String snapshotRefId) {
        this.snapshotRefId = snapshotRefId;
    }

}
