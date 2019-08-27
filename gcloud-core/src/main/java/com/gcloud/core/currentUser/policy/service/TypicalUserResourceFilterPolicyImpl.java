package com.gcloud.core.currentUser.policy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gcloud.core.currentUser.enums.RoleType;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.util.SqlUtil;
import com.gcloud.header.api.model.CurrentUser;

@Service
public class TypicalUserResourceFilterPolicyImpl implements IUserResourceFilterPolicy {
	@Override
	public FilterPolicyModel filterPolicy(CurrentUser currentUser, String prefix) {
		FilterPolicyModel model = new FilterPolicyModel();
		List<Object> params = new ArrayList<Object>();
		StringBuffer sqlWhereBuff = new StringBuffer();
		sqlWhereBuff.append(" AND ");

		// 超级管理员
		if (currentUser.getRole().equals(RoleType.SUPER_ADMIN.getRoleId())) {
			sqlWhereBuff.append(" 1 = 1 ");
			model.setParams(params);
			model.setWhereSql(sqlWhereBuff.toString());
			return model;
		} else {
			if(currentUser.getUserTenants().size() > 0) {
				if(!StringUtils.isBlank(currentUser.getDefaultTenant())) {
						sqlWhereBuff.append( prefix + "tenant_id = ? AND ");
						params.add(currentUser.getDefaultTenant());
				}
				sqlWhereBuff.append( prefix + "tenant_id IN ( ");
				sqlWhereBuff.append(SqlUtil.inPreStr(currentUser.getUserTenants().size()));
				sqlWhereBuff.append(")");
				params.addAll(currentUser.getUserTenants());
			} else {
				sqlWhereBuff.append( "1 != 1");
			}
		}

		model.setParams(params);
		model.setWhereSql(sqlWhereBuff.toString());
		
		return model;
	}
}
