package com.gcloud.core.currentUser.policy.service;

import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.header.api.model.CurrentUser;

public interface IUserResourceFilterPolicy {
	FilterPolicyModel filterPolicy(CurrentUser currentUser, String prefix);
}
