package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;

@Component
public class SubnetNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		SubnetDao subnetDao = SpringUtil.getBean(SubnetDao.class);
		List<Subnet> list = subnetDao.findAll();
		for(Subnet item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.SUBNET_NAME;
	}

	@Override
	public String getValue(String key) {
		SubnetDao subnetDao = SpringUtil.getBean(SubnetDao.class);
		Subnet subnet = subnetDao.getById(key);
		return subnet!=null?(subnet.getName()==null?subnet.getId():subnet.getName()):null;
	}

}
