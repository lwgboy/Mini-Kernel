package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class PortNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		PortDao portDao = SpringUtil.getBean(PortDao.class);
		List<Port> list = portDao.findAll();
		for(Port item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.PORT_NAME;
	}

	@Override
	public String getValue(String key) {
		PortDao portDao = SpringUtil.getBean(PortDao.class);
		Port port = portDao.getById(key);
		return port!=null?(port.getName()==null?port.getId():port.getName()):null;
	}

}
