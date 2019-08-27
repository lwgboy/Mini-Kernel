package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class NetworkResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private NetworkDao networkDao;
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Network network = networkDao.getById(resourceId);
		return network==null?null:network.getTenantId();
	}
}
