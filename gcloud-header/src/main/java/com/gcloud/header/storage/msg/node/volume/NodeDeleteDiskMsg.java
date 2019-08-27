
package com.gcloud.header.storage.msg.node.volume;

import java.util.ArrayList;
import java.util.List;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.ResourceProviderVo;

public class NodeDeleteDiskMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String volumeId;
    private String storageType;
    private String poolName;
    private String driverName;

    private List<ResourceProviderVo> snapshots = new ArrayList<>();

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

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

    public List<ResourceProviderVo> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<ResourceProviderVo> snapshots) {
        this.snapshots = snapshots;
    }

}
