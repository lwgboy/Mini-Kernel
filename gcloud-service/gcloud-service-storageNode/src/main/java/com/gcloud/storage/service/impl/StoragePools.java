
package com.gcloud.storage.service.impl;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.SnapshotType;
import com.gcloud.storage.NodeStoragePoolVo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StoragePools {

    private static final Map<String, NodeStoragePoolVo> POOLS = new HashMap<>();

    public void add(Integer provider, String storageType, String poolName, String categoryCode, String snapshotType) {
        NodeStoragePoolVo poolVo = new NodeStoragePoolVo();
        poolVo.setProvider(provider);
        poolVo.setStorageType(storageType);
        poolVo.setPoolName(poolName);
        poolVo.setCategoryCode(categoryCode);
        poolVo.setSnapshotType(SnapshotType.get(snapshotType));
        POOLS.put(poolVo.getPoolName(), poolVo);
    }

    public NodeStoragePoolVo get(String poolName) {
        return POOLS.get(poolName);
    }

    public NodeStoragePoolVo checkAndGet(String poolName) throws GCloudException {
        NodeStoragePoolVo pool = POOLS.get(poolName);
        if (pool == null) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_POOL);
        }
        return pool;
    }

}
