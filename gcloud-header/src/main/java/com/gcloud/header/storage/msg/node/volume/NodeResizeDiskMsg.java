
package com.gcloud.header.storage.msg.node.volume;

import com.gcloud.header.NodeMessage;

public class NodeResizeDiskMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String volumeId;
    private String storageType;
    private String poolName;
    private String driverName;
    private Integer oldSize;
    private Integer newSize;

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

    public Integer getOldSize() {
        return oldSize;
    }

    public void setOldSize(Integer oldSize) {
        this.oldSize = oldSize;
    }

    public Integer getNewSize() {
        return newSize;
    }

    public void setNewSize(Integer newSize) {
        this.newSize = newSize;
    }

}
