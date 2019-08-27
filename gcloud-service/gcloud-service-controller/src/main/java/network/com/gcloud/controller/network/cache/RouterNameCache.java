package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class RouterNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		Map<String,String> result=new HashMap<String, String>();
		RouterDao routerDao = SpringUtil.getBean(RouterDao.class);
		List<Router> list = routerDao.findAll();
		for(Router item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.ROUTER_NAME;
	}

	@Override
	public String getValue(String key) {
		RouterDao routerDao = SpringUtil.getBean(RouterDao.class);
		Router router = routerDao.getById(key);
		return router!=null?(router.getName()==null?router.getId():router.getName()):null;
	}

}
