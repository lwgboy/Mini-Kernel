package com.gcloud.controller.network.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.network.entity.SecurityGroupRule;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

@Repository
public class SecurityGroupRuleDao extends JdbcBaseDaoImpl<SecurityGroupRule, String>{
	public void deleteBySecurityGroup(String securityGroupId) {
		String sql = "delete from gc_security_group_rules ";
		sql += "where security_group_id='" + securityGroupId + "' ";
		
	    this.jdbcTemplate.execute(sql);
	}
}
