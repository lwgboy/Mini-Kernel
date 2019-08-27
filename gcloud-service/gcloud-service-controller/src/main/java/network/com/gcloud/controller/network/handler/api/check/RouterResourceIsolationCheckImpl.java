package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class RouterResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private RouterDao routerDao;
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Router router = routerDao.getById(resourceId);
		return router==null?null:router.getTenantId();
	}

}
