
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.controller.storage.driver.GcloudStorageDrivers;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.provider.IStoragePoolProvider;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.model.StoragePoolInfo;

import org.springframework.stereotype.Component;

@Component
public class GcloudStoragePoolProvider implements IStoragePoolProvider {

    @Override
    public ResourceType resourceType() {
        return ResourceType.STORAGE_POOL;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    @Override
    public String createStoragePool(String poolId, String storageType, String poolName, String hostname, String taskId) throws GCloudException {
        GcloudStorageDrivers.get(storageType).createStoragePool(poolId, poolName, hostname, taskId);
        return poolId;
    }

    @Override
    public void deleteStoragePool(String storageType, String providerRefId, String poolName) throws GCloudException {
        GcloudStorageDrivers.get(storageType).deleteStoragePool(poolName);
    }

	@Override
	public StoragePoolInfo getPoolSize(StoragePool pool) {
		return GcloudStorageDrivers.get(pool.getStorageType()).getStoragePool(pool);
	}

}
