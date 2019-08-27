package com.gcloud.controller.storage.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class StoragePoolNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		StoragePoolDao storagePoolDao = SpringUtil.getBean(StoragePoolDao.class);
		List<StoragePool> list = storagePoolDao.findAll();
		for(StoragePool item: list){
			result.put(item.getId(),item.getDisplayName()==null?item.getId():item.getDisplayName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.STORAGEPOOL_NAME;
	}

	@Override
	public String getValue(String key) {
		StoragePoolDao storagePoolDao = SpringUtil.getBean(StoragePoolDao.class);
		StoragePool storagePool = storagePoolDao.getById(key);
		return storagePool!=null?(storagePool.getDisplayName()==null?storagePool.getId():storagePool.getDisplayName()):null;
	}

}
