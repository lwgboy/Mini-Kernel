package com.gcloud.controller.storage.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class SnapshotResourceIsolationCheckImpl extends ResourceIsolationCheckImpl {
	@Autowired
    private SnapshotDao snapshotDao;

	@Override
	public String getResourceTenantId(String resourceId) {
		Snapshot snapshot = snapshotDao.getById(resourceId);
		return snapshot==null?null:snapshot.getTenantId();
	}

}
