package com.gcloud.controller.compute.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;

@Component
public class InstanceAliasCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		InstanceDao instanceDao = SpringUtil.getBean(InstanceDao.class);
		List<VmInstance> list = instanceDao.findAll();
		for(VmInstance instance: list){
			result.put(instance.getId(),instance.getAlias()==null?instance.getId():instance.getAlias());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.INSTANCE_ALIAS;
	}

	@Override
	public String getValue(String key) {
		InstanceDao instanceDao = SpringUtil.getBean(InstanceDao.class);
		VmInstance instance = instanceDao.getById(key);
		return instance!=null?(instance.getAlias()==null?instance.getId():instance.getAlias()):null;
	}

}
