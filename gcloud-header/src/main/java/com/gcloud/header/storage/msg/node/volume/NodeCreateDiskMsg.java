
package com.gcloud.header.storage.msg.node.volume;

import com.gcloud.header.NodeMessage;

public class NodeCreateDiskMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String storageType;
    private String poolName;
    private String driverName;
    private String volumeId;
    private Integer size;
    private String imageId;

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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}
