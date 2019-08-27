package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class EipResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    FloatingIpDao floatingipDao;; 
	
	@Override
	public String getResourceTenantId(String resourceId) {
		FloatingIp floatingIp = floatingipDao.getById(resourceId);
		return floatingIp==null?null:floatingIp.getTenantId();
	}

}
