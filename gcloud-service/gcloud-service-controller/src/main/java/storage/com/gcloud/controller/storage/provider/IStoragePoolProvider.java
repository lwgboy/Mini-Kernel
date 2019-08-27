
package com.gcloud.controller.storage.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.storage.model.StoragePoolInfo;

public interface IStoragePoolProvider extends IResourceProvider {

    String createStoragePool(String poolId, String storageType, String poolName, String hostname, String taskId) throws GCloudException;

    void deleteStoragePool(String storageType, String providerRefId, String poolName) throws GCloudException;
    
    StoragePoolInfo getPoolSize(StoragePool pool);

}
