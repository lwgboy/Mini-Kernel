package com.gcloud.core.cache.redis;

import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.SerializeUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class RedisCacheContainer extends CacheContainer {
	
	private static GCloudRedisTemplate redisClient;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		redisClient = SpringUtil.getBean(GCloudRedisTemplate.class);
	}
	
	@Override
	public Object get(CacheType type, String key) {
		Object result = null;
		try {
			String res = String.valueOf(redisClient.opsForHash().get(getDistributeType(type), key));

			if (res.equals("null")) {
				Cache<?> cache = Cache.getMember().get(type);
				if(cache == null) {
					return null;
				}
				Object o = cache.getValue(key);
				if (null != o) {
					res = SerializeUtils.serialize(o);
					redisClient.opsForHash().put(getDistributeType(type), key, SerializeUtils.serialize(res));
				} else {
					return null;
				}
			}
			result = SerializeUtils.unSerialize(res);
		} catch (GCloudException e) {
			log.error("【RedisCacheContainer】获取缓存失败：" + getDistributeType(type) + "," + key);
		}
		return result;
	}

	@Override
	public String getString(CacheType type, String key) {
		String res = null;
		String result = null;
		try {
			result = String.valueOf(redisClient.opsForHash().get(getDistributeType(type), key));

			if (result.equals("null")) {
				Cache<?> cache = Cache.getMember().get(type);
				if(cache == null) {
					return null;
				}
				Object obj = cache.getValue(key);
				if (null != obj) {
					redisClient.opsForHash().put(getDistributeType(type), key, SerializeUtils.serialize(obj));
					res = obj.toString();
				} else {
					return null;
				}
			}
			else
			{
				res = SerializeUtils.unSerialize(result).toString();
			}
		} catch (GCloudException e) {
			log.error("RedisCacheContainer获取缓存失败：" + getDistributeType(type) + "," + key);
		}
		return res;
	}

	@Override
	public void put(CacheType type, String key, Object value) {
		// TODO Auto-generated method stub
		redisClient.opsForHash().put(getDistributeType(type), key,// value.toString());
				SerializeUtils.serialize(value));
	}

	@Override
	public <T> void put(CacheType type, Map<String, T> map) {
		// TODO Auto-generated method stub
		Map<String, String> results = new HashMap<String, String>();
		Iterator<Entry<String, T>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, T> entry = it.next();
			results.put(entry.getKey().toString(), //entry.getValue().toString());
					SerializeUtils.serialize(entry.getValue()));
		}
		redisClient.delete(getDistributeType(type));
		if(!results.isEmpty())
		{
			redisClient.opsForHash().putAll(getDistributeType(type), results);
		}
	}

	@Override
	public void remove(CacheType type, String key) {
		// TODO Auto-generated method stub
		redisClient.opsForHash().delete(getDistributeType(type), key);
	}
	
	public String getDistributeType(CacheType type) {
		return MessageFormat.format("gcloud_cache_{0}", type.name());
	}

	@Override
	public void put(String key, Object value, int expire) {
		redisClient.opsForValue().set(key, SerializeUtils.serialize(value), Duration.ofSeconds(expire));
	}

	@Override
	public void refresh(String key, int expire) {
		redisClient.expire(key, expire, TimeUnit.SECONDS);
	}

	@Override
	public Object get(String key) {
		Object result = null;
		try {
			String res = String.valueOf(redisClient.opsForValue().get(key));

			if (res == null) {
				return null;
			}
			result = SerializeUtils.unSerialize(res);
		} catch (GCloudException e) {
			
		}
		return result;
	}

	@Override
	public void remove(String key) {
		redisClient.delete( key);
	}
}
