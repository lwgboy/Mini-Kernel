package com.gcloud.core.cache.redis.lock.util;

import com.gcloud.core.cache.DistributedLock;
import com.gcloud.core.cache.redis.lock.RedisSpinLock;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockUtil {

	public static String spinLock(String lockName, long lockTimeout, long getLockTimeout){

        DistributedLock redisLock = SpringUtil.getApplicationContext().getBean(RedisSpinLock.class);
        return redisLock.getLock(lockName, lockTimeout, getLockTimeout);
    }

    public static String spinLock(String lockName, long lockTimeout, long getLockTimeout, String errorCode) throws GCloudException{

        String lockId;
        try{
            lockId = spinLock(lockName, lockTimeout, getLockTimeout);
        }catch (Exception ex){
            log.error(String.format("获取锁失败,lock=%s", lockName), ex);
            throw new GCloudException(errorCode);
        }

        return lockId;
    }


    public static void releaseSpinLock(String lockName, String lockId){
    	DistributedLock redisLock = SpringUtil.getApplicationContext().getBean(RedisSpinLock.class);
        try{
            redisLock.releaseLock(lockName, lockId);
        }catch (Exception ex){
            log.error(String.format("获取锁失败,lock=%s,lockId=%s", lockName, lockId), ex);
        }

    }

}
