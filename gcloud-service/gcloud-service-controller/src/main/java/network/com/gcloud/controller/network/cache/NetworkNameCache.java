package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class NetworkNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		NetworkDao networkDao = SpringUtil.getBean(NetworkDao.class);
		List<Network> list = networkDao.findAll();
		for(Network item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.NETWORK_NAME;
	}

	@Override
	public String getValue(String key) {
		NetworkDao networkDao = SpringUtil.getBean(NetworkDao.class);
		Network network = networkDao.getById(key);
		return network!=null?(network.getName()==null?network.getId():network.getName()):null;
	}

}
