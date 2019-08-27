
package com.gcloud.storage;

import com.gcloud.header.storage.enums.SnapshotType;

import lombok.Data;

@Data
public class NodeStoragePoolVo {

    private Integer provider;
    private String storageType;
    private String poolName;
    private String categoryCode;
    private SnapshotType snapshotType;

}
