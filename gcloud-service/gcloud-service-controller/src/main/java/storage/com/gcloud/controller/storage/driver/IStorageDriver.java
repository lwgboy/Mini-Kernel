
package com.gcloud.controller.storage.driver;

import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.storage.model.StoragePoolInfo;

public interface IStorageDriver {

    StorageType storageType();

    // STORAGE POOL

    void createStoragePool(String poolId, String poolName, String hostname, String taskId) throws GCloudException;

    void deleteStoragePool(String poolName) throws GCloudException;
    
    StoragePoolInfo getStoragePool(StoragePool pool) throws GCloudException;

    // VOLUME

    CreateDiskResponse createVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException;

    void deleteVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException;

    void resizeVolume(String taskId, StoragePool pool, Volume volume, int newSize) throws GCloudException;

    void createSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot) throws GCloudException;

    void deleteSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, String taskId) throws GCloudException;

    void resetSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, Integer size) throws GCloudException;

//    List<Volume> listVolume() throws GCloudException;
//    List<Snapshot> listSnapshot() throws GCloudException;
}
