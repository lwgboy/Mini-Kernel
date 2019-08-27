
package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

public class DescribeStoragePoolsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<StoragePoolModel> storagePools;

    public List<StoragePoolModel> getStoragePools() {
        return storagePools;
    }

    public void setStoragePools(List<StoragePoolModel> storagePools) {
        this.storagePools = storagePools;
    }

}
