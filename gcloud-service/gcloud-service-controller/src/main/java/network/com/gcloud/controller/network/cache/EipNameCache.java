package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class EipNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		FloatingIpDao floatingIpDao = SpringUtil.getBean(FloatingIpDao.class);
		List<FloatingIp> list = floatingIpDao.findAll();
		for(FloatingIp item: list){
			result.put(item.getId(),item.getFloatingIpAddress()==null?item.getId():item.getFloatingIpAddress());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.EIP_NAME;
	}

	@Override
	public String getValue(String key) {
		FloatingIpDao floatingIpDao = SpringUtil.getBean(FloatingIpDao.class);
		FloatingIp floatingIp = floatingIpDao.getById(key);
		return floatingIp!=null?(floatingIp.getFloatingIpAddress()==null?floatingIp.getId():floatingIp.getFloatingIpAddress()):null;
	}

}
