
package com.gcloud.storage.service.impl;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.storage.driver.StorageDrivers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeSnapshotServiceImpl {

    @Autowired
    private StorageDrivers drivers;

    @Autowired
    private StoragePools pools;

    public void createSnapshot(String storageType, String poolName, String driverName, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException {
        drivers.get(driverName).createSnapshot(pools.checkAndGet(poolName), volumeRefId, snapshotId, snapshotRefId);
    }

    public void deleteSnapshot(String storageType, String poolName, String driverName, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException {
        drivers.get(driverName).deleteSnapshot(pools.checkAndGet(poolName), volumeRefId, snapshotId, snapshotRefId);
    }

    public List<String> resetSnapshot(String storageType, String poolName, String driverName, String volumeRefId, String snapshotId, String snapshotRefId, Integer size)
            throws GCloudException {
        return drivers.get(driverName).resetSnapshot(pools.checkAndGet(poolName), volumeRefId, snapshotId, snapshotRefId, size);
    }

}
