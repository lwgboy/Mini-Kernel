package com.gcloud.core.currentUser.policy.enums;
import com.gcloud.core.currentUser.policy.service.*;

public enum UserResourceFilterPolicy {
	TYPICAL(TypicalUserResourceFilterPolicyImpl.class);

	private Class<?> filterPolicyClazz;
	
	UserResourceFilterPolicy(Class<?> filterPolicyClazz) {
		this.filterPolicyClazz = filterPolicyClazz;
	}

	public Class<?> getFilterPolicyClazz() {
		return filterPolicyClazz;
	}
}
