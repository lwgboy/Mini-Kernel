package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.SecurityGroupPortBinding;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/25.
 */
@Repository
public class SecurityGroupPortBindingDao extends JdbcBaseDaoImpl<SecurityGroupPortBinding, Long> {
	public void deleteByPortSecurityGroup(String portId, String securityGroupId) {
		String sql = "delete from gc_security_group_port_bindings ";
		sql += "where port_id='" + portId + "' ";
		if(null != securityGroupId) {
			sql += "and security_group_id='" + securityGroupId + "'";
		}
		
	    this.jdbcTemplate.execute(sql);
	}
	
	public void deleteBySecurityGroup(String securityGroupId) {
		String sql = "delete from gc_security_group_port_bindings ";
		sql += "where security_group_id='" + securityGroupId + "' ";
		
	    this.jdbcTemplate.execute(sql);
	}
	
    public int deleteByPortId(String portId) {
        String sql = "delete from gc_security_group_port_bindings where port_id = ?";
        Object[] values = {portId};
        return this.jdbcTemplate.update(sql, values);
    }

}
