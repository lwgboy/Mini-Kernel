package com.gcloud.controller.storage.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class SnapshotNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
		List<Snapshot> list = snapshotDao.findAll();
		for(Snapshot item: list){
			result.put(item.getId(),item.getDisplayName()==null?item.getId():item.getDisplayName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.SNAPSHOT_NAME;
	}

	@Override
	public String getValue(String key) {
		SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
		Snapshot snapshot = snapshotDao.getById(key);
		return snapshot!=null?(snapshot.getDisplayName()==null?snapshot.getId():snapshot.getDisplayName()):null;
	}

}
