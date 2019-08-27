package com.gcloud.core.cache;

import java.util.HashMap;
import java.util.Map;

import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;

public abstract class Cache<T> {
	private boolean hasInit=false;
	private static Map<CacheType,Cache> caches=new HashMap<CacheType, Cache>();
	public static void register(Class c){
		Cache cache=null;
		try {
			cache = (Cache) SpringUtil.getBean(c);
			caches.put(cache.getType(), cache);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Map<CacheType,Cache> getMember(){
		return caches;
	}
	
	public abstract Map<String,T> requestCache();
	public abstract CacheType getType();
	public abstract T getValue(String key);
	
	public void init(){
		Map<String,T> map=null;
		try{
			map=requestCache();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(map!=null){
			CacheContainer container = CacheContainer.getInstance();
			container.put(getType(), map);
			hasInit=true;
		}
	}
	
	/**
	 * 是否定时重新初始化
	 * @return
	 */
	public boolean reInit(){
		return true;
	}
	/**
	 * 初始化的时间隔，单秒
	 * @return
	 */
	public int interval(){
		return 60;
	}

	public boolean isHasInit() {
		return hasInit;
	}

	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}
}
