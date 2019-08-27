package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class PortResourceIsolationCheckImpl extends ResourceIsolationCheckImpl{
	@Autowired
    private PortDao portDao; 
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Port port = portDao.getById(resourceId);
		return port==null?null:port.getTenantId();
	}

}
