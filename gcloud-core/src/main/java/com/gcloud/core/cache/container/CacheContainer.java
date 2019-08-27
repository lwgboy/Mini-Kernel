package com.gcloud.core.cache.container;

import java.util.Map;

import org.springframework.core.env.Environment;

import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;

public abstract class CacheContainer {
	
	private static CacheContainer instance = null;
	
	public static CacheContainer getInstance() {
		if(null == instance)
		{
			Environment env = (Environment)SpringUtil.getBean("environment");
			String cacheContainerType = env.getProperty("spring.cache.type", "SIMPLE");
			instance = (CacheContainer)SpringUtil.getBean( cacheContainerType.toLowerCase() + "CacheContainer");
			instance.init();
		}
		return instance;
	}
	public abstract void init();
	public abstract Object get(CacheType type,String key);
	public abstract String getString(CacheType type,String key);
	public abstract void put(CacheType type,String key,Object value);
	public abstract <T> void put(CacheType type,Map<String,T> map);
	public abstract void remove(CacheType type,String key);
	public abstract void put(String key, Object value, int expire);
	public abstract Object get(String key);
	public abstract void refresh(String key, int expire);
	public abstract void remove(String key);
}
