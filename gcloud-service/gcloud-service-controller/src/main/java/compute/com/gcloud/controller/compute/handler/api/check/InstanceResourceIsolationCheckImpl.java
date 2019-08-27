package com.gcloud.controller.compute.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;
@Service
public class InstanceResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private InstanceDao instanceDao; 

	@Override
	public String getResourceTenantId(String resourceId) {
		VmInstance instance = instanceDao.getById(resourceId);
		return instance==null?null:instance.getTenantId();
	}

}
