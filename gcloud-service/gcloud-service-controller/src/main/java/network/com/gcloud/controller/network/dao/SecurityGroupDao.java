package com.gcloud.controller.network.dao;

import com.gcloud.common.model.PageParams;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/25.
 */
@Repository
public class SecurityGroupDao extends JdbcBaseDaoImpl<SecurityGroup, String> {
	
	public <E> PageResult<E> describeSecurityGroups(PageParams params, Class<E> clazz, CurrentUser currentUser){
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "s.");
		List<Object> listParams = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();

        sql.append("select s.id as securityGroupId,s.name as securityGroupName,s.description from gc_security_groups s where 1 = 1 ");
        sql.append(sqlModel.getWhereSql());
        listParams.addAll(sqlModel.getParams());
        sql.append(" order by s.create_time desc");

        return findBySql(sql.toString(), listParams, params.getPageNumber(), params.getPageSize(), clazz);
	}
}
