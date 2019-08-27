package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class SecurityGroupResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private SecurityGroupDao securityGroupDao;

	@Override
	public String getResourceTenantId(String resourceId) {
		SecurityGroup sg = securityGroupDao.getById(resourceId);
		return sg==null?null:sg.getTenantId();
	}

}
