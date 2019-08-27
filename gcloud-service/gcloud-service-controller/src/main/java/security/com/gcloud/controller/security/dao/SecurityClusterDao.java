package com.gcloud.controller.security.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.security.entity.SecurityCluster;
import com.gcloud.controller.security.model.ApiListSecurityCluseterParams;
import com.gcloud.controller.security.model.DescribeSecurityClusterParams;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import com.gcloud.header.api.model.CurrentUser;

@Jdbc("controllerJdbcTemplate")
@Repository
public class SecurityClusterDao extends JdbcBaseDaoImpl<SecurityCluster, String> {

    public int changeState(String id, String state){
        String sql = "update gc_security_cluster set state = ?, update_time = now() where id = ?";
        Object[] values = {state, id};
        return this.jdbcTemplate.update(sql, values);
    }
    
    public <E> PageResult<E> describeSecurityCluster(DescribeSecurityClusterParams params, Class<E> clazz, CurrentUser currentUser) {
    	
    	IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "i.");
		
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		sql.append("select c.*, (select s.cidr from gc_security_cluster_subnet as s where c.id = s.cluster_id limit 1) as protectionNetCidr from gc_security_cluster as c");
		sql.append(" where 1 = 1");
		
		if(StringUtils.isNotBlank(params.getSecurityClusterName())) {
			sql.append(" and c.name like concat('%', ?, '%')");
			values.add(params.getSecurityClusterName());
		}
		
		sql.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());
		
		sql.append(" order by c.create_time desc");
		
		return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
    }
    
    public <E> PageResult<E> apiPageSecurityCluster(ApiListSecurityCluseterParams params, Class<E> clazz) {
    	StringBuffer sql = new StringBuffer();
    	sql.append("select * from gc_security_cluster");
    	return this.findBySql(sql.toString(), params.getPageNumber(), params.getPageSize(), clazz);
    }
}
