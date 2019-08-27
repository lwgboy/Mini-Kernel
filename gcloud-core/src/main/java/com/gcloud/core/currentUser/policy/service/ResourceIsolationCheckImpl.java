package com.gcloud.core.currentUser.policy.service;

import org.apache.commons.lang.StringUtils;

import com.gcloud.core.currentUser.enums.RoleType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;

public abstract class ResourceIsolationCheckImpl implements IResourceIsolationCheck {

	@Override
	public void check(String resourceId, CurrentUser currentUser) throws GCloudException {
		if(!currentUser.getRole().equals(RoleType.SUPER_ADMIN.getRoleId())) {
			String resourceTenantId = getResourceTenantId(resourceId);
			if(StringUtils.isNotBlank(currentUser.getDefaultTenant())) {
				if(!currentUser.getDefaultTenant().equals(resourceTenantId)) {
					throw new GCloudException("当前用户没有权限操作该资源");
				}
			}else if(!currentUser.getUserTenants().contains(resourceTenantId)) {
				throw new GCloudException("当前用户没有权限操作该资源");
			}
		}
	}
	
	public abstract String getResourceTenantId(String resourceId);
}
