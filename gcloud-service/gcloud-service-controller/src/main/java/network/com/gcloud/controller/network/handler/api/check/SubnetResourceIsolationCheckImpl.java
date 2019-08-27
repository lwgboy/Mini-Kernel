package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class SubnetResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private SubnetDao subnetDao;
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Subnet subnet = subnetDao.getById(resourceId);
		return subnet==null?null:subnet.getTenantId();
	}

}
