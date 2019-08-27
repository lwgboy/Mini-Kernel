package com.gcloud.core.currentUser.policy.service;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;

public interface IResourceIsolationCheck {
	void check(String resourceId, CurrentUser currentUser)  throws GCloudException;
}
