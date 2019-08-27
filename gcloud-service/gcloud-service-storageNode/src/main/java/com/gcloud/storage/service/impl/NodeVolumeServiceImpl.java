
package com.gcloud.storage.service.impl;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.ResourceProviderVo;
import com.gcloud.storage.driver.StorageDrivers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeVolumeServiceImpl {

    @Autowired
    private StorageDrivers drivers;

    @Autowired
    private StoragePools pools;

    public void createDisk(String storageType, String poolName, String driverName, String volumeId, Integer size, String imageId) throws GCloudException {
        drivers.get(driverName).createDisk(pools.checkAndGet(poolName), volumeId, size, imageId);
    }

    public void deleteDisk(String storageType, String poolName, String driverName, String volumeId, List<ResourceProviderVo> snapshots) throws GCloudException {
        drivers.get(driverName).deleteDisk(pools.checkAndGet(poolName), volumeId, snapshots);
    }

    public void resizeDisk(String storageType, String poolName, String driverName, String volumeId, Integer oldSize, Integer newSize) throws GCloudException {
        drivers.get(driverName).resizeDisk(pools.checkAndGet(poolName), volumeId, oldSize, newSize);
    }

}
