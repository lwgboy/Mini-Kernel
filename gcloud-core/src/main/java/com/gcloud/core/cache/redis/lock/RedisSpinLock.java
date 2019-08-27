package com.gcloud.core.cache.redis.lock;

import com.gcloud.core.cache.DistributedLock;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.exception.GCloudException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnExpression("${gcloud.redis.enable:false} == true")
public class RedisSpinLock extends DistributedLock {
	@Autowired
	private GCloudRedisTemplate redisTemplate;
	
	@Override
	public String getLock(String lockName, long lockTimeout, long getLockTimeout) throws GCloudException {
		// TODO Auto-generated method stub
		boolean isSucc = false;
		boolean isGetTimerout = false;

		String value = UUID.randomUUID().toString(); // 这个值用于删除
		long startTime = System.currentTimeMillis();
		do {
			isSucc = redisTemplate.opsForValue().setIfAbsent(lockName, value, Duration.ofMillis(lockTimeout));

			if (getLockTimeout >= 0) {
				isGetTimerout = System.currentTimeMillis() - startTime > getLockTimeout;
			}

		} while (!isSucc && !isGetTimerout);

		// 不成功直接抛错
		if (!isSucc) {
			throw new GCloudException("::get lock fail");
		}
		return value;
	}

	@Override
	public void releaseLock(String lockName, String value) throws GCloudException {
		// TODO Auto-generated method stub
		try{
			String v = String.valueOf(redisTemplate.opsForValue().get(lockName));
			if (v != null && v.equals(value)) {
				redisTemplate.delete(lockName);
			}
		}catch (Exception ex){
			throw new GCloudException(String.format("释放锁失败%s", ex.getMessage()));
		}
	}

}
