package com.gcloud.core.cache.ehcache;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
public class EhcacheCacheContainer extends CacheContainer {
	
	private Map<String,Object> locks = new HashMap<String, Object>();
	
	//ehcache.xml 保存在src/main/resources/
    private static final String path = "/ehcache.xml";
 
    private URL url;
 
    private static CacheManager manager;
 
	@Override
	public void init() {
		if(null == manager)
		{
			url = getClass().getResource(path);
	        manager = CacheManager.create(url);
		}
	}

	@Override
	public Object get(CacheType type, String key) {
		Cache cache = manager.getCache(getType(type));
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
	}

	@Override
	public String getString(CacheType type, String key) {
		Cache cache = manager.getCache(getType(type));
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue().toString();
	}

	@Override
	public void put(CacheType type, String key, Object value) {
		Object lock = locks.get(getType(type));
		if(lock == null){
			lock = new Object();
			locks.put(getType(type), lock);
		}
		synchronized (lock) {
			Cache cache = manager.getCache(getType(type));
	        Element element = new Element(key, value);
	        cache.put(element);
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
			Cache cache = manager.getCache(getType(type));
			cache.removeAll();
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>) map;
			for (Map.Entry<String, Object> entry : tmp.entrySet()) { 
				Element element = new Element(entry.getKey(), entry.getValue());  
				cache.put(element);
			}
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
			Cache cache = manager.getCache(getType(type));
	        cache.remove(key);
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
		// TODO Auto-generated method stub
		
	}

}
