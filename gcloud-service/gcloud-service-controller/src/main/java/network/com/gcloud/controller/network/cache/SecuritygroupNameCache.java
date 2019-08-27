package com.gcloud.controller.network.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.service.SpringUtil;
@Component
public class SecuritygroupNameCache extends Cache<String> {

	@Override
	public Map<String, String> requestCache() {
		
		Map<String,String> result=new HashMap<String, String>();
		SecurityGroupDao securityGroupDao = SpringUtil.getBean(SecurityGroupDao.class);
		List<SecurityGroup> list = securityGroupDao.findAll();
		for(SecurityGroup item: list){
			result.put(item.getId(),item.getName()==null?item.getId():item.getName());
		}
		return result;
	}

	@Override
	public CacheType getType() {
		return CacheType.SECURITYGROUP_NAME;
	}

	@Override
	public String getValue(String key) {
		SecurityGroupDao securityGroupDao = SpringUtil.getBean(SecurityGroupDao.class);
		SecurityGroup securityGroup = securityGroupDao.getById(key);
		return securityGroup!=null?(securityGroup.getName()==null?securityGroup.getId():securityGroup.getName()):null;
	}

}
