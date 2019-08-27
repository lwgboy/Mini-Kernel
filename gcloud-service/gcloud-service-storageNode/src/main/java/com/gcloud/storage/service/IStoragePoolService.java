
package com.gcloud.storage.service;

import com.gcloud.header.storage.model.StoragePoolInfo;

public interface IStoragePoolService {
	StoragePoolInfo getLocalPoolInfo(String poolName);
}
