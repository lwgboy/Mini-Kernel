package com.gcloud.controller.network.handler.api.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.network.dao.SecurityGroupRuleDao;
import com.gcloud.controller.network.entity.SecurityGroupRule;
import com.gcloud.core.currentUser.policy.service.ResourceIsolationCheckImpl;

@Service
public class SecurityGroupRuleResourceIsolationCheckImpl  extends ResourceIsolationCheckImpl {
	@Autowired
    private SecurityGroupRuleDao securityGroupRuleDao;

	@Override
	public String getResourceTenantId(String resourceId) {
		SecurityGroupRule sgRule = securityGroupRuleDao.getById(resourceId);
		return sgRule==null?null:sgRule.getTenantId();
	}
}
