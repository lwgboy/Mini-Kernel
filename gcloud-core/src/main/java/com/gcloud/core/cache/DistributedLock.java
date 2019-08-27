package com.gcloud.core.cache;

import com.gcloud.core.exception.GCloudException;

public abstract class DistributedLock {
	public abstract String getLock(String lockName, long lockTimeout, long getLockTimeout) throws GCloudException;

	public abstract void releaseLock(String lockName, String value) throws GCloudException;


}
