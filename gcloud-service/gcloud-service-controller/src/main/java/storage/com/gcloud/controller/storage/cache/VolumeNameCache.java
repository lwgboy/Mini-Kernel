package com.gcloud.controller.storage.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class VolumeNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
		List<Volume> list = volumeDao.findAll();
		for(Volume item: list){
			result.put(item.getId(),item.getDisplayName()==null?item.getId():item.getDisplayName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.VOLUME_NAME;
	}

	@Override
	public String getValue(String key) {
		VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
		Volume volume = volumeDao.getById(key);
		return volume!=null?(volume.getDisplayName()==null?volume.getId():volume.getDisplayName()):null;
	}

}
