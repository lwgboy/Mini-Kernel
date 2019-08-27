package com.gcloud.core.cache;

import com.gcloud.core.exception.GCloudException;

public abstract class KeyValueDistributedLock {
    // 指定锁的内容
    public abstract boolean getLock(String lockName, String value, long expireSecondTime) throws GCloudException;
    // 返回锁的内容
    public abstract String getValueOfLock(String lockName) throws GCloudException;

    public abstract long releaseLock(String lockName) throws GCloudException;
}
