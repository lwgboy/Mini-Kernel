package com.gcloud.core.cache.simple;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;

@Component
public class SimpleCacheContainer extends CacheContainer {
	
	private Map<String,Map<String,Object>> caches = new HashMap<String, Map<String,Object>>();
	private Map<String,Object> locks = new HashMap<String, Object>();

	@Override
	public void init() {

	}

	@Override
	public Object get(CacheType type, String key) {
		Map<String,Object> map=caches.get(getType(type));
		if(map==null) {
			map=new HashMap<String, Object>();
		}
		Object result= map.get(key);
		if(result == null){
			Cache<?> cache = Cache.getMember().get(type);
			if(cache == null) {
				return null;
			}
			Object o = cache.getValue(key);
			if(o!=null){
				map.put(key, o);
			}
			result= map.get(key);
		}
		return result;
	}

	@Override
	public String getString(CacheType type, String key) {
		Map<String,Object> map=caches.get(getType(type));
		if(map==null) {
			map=new HashMap<String, Object>();
		}
		Object result= map.get(key);
		if(result == null){
			Cache<?> cache = Cache.getMember().get(type);
			if(cache == null) {
				return null;
			}
			Object o = cache.getValue(key);
			if(o!=null){
				map.put(key, o);
			}
			result= map.get(key);
		}
		return result.toString();
	}

	@Override
	public void put(CacheType type, String key, Object value) {
		Object lock = locks.get(getType(type));
		if(lock == null){
			lock = new Object();
			locks.put(getType(type), lock);
		}
		synchronized (lock) {
			Map<String,Object> map = caches.get(getType(type));
			if(map == null){
				map = new HashMap<String, Object>();
				caches.put(getType(type), map);
			}
			map.put(key, value);
		}
	}

	@Override
	public <T> void put(CacheType type, Map<String, T> map) {
		Object lock = locks.get(getType(type));
		if(lock == null){
			lock = new Object();
			locks.put(getType(type), lock);
		}
		
		synchronized(lock) {
			caches.remove(getType(type));
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp=(Map<String, Object>) map;
			caches.put(getType(type), tmp);
		}
	}

	@Override
	public void remove(CacheType type, String key) {
		Object lock = locks.get(getType(type));
		if(lock == null){
			lock = new Object();
			locks.put(getType(type), lock);
		}
		synchronized(lock) {
			Map<String,Object> map = caches.get(getType(type));
			if(map!=null) {
				map.remove(key);
			}
		}
	}
	
	public String getType(CacheType type) {
		return MessageFormat.format("gcloud_cache_{0}", type.name());
	}

	@Override
	public void put(String key, Object value, int expire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(String key, int expire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String key) {
		
	}

}
