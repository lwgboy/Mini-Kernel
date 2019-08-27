package com.gcloud.controller.storage.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class VolumeResourceIsolationCheckImpl extends ResourceIsolationCheckImpl{
	@Autowired
    private VolumeDao volumeDao;
	
	@Override
	public String getResourceTenantId(String resourceId) {
		Volume volume = volumeDao.getById(resourceId);
		return volume==null?null:volume.getTenantId();
	}

}
