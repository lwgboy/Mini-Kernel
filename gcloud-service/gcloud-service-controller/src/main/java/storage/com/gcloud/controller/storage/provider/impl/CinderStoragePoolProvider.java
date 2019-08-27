
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.provider.IStoragePoolProvider;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.model.StoragePoolInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CinderStoragePoolProvider implements IStoragePoolProvider {

    @Autowired
    private CinderProviderProxy proxy;

    @Override
    public ResourceType resourceType() {
        return ResourceType.STORAGE_POOL;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.CINDER;
    }

    @Override
    public String createStoragePool(String poolId, String storageType, String poolName, String hostname, String taskId) throws GCloudException {
        return this.proxy.createVolumeType(poolName, poolName);
    }

    @Override
    public void deleteStoragePool(String storageType, String providerRefId, String poolName) throws GCloudException {
        this.proxy.deleteVolumeType(providerRefId);
    }

	@Override
	public StoragePoolInfo getPoolSize(StoragePool pool) {
		return this.proxy.getPoolInfo(pool);
	}

}
