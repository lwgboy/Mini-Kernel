package com.gcloud.controller.security.dao;

import com.gcloud.controller.security.entity.SecurityClusterSubnet;
import com.gcloud.controller.security.enums.SecurityNetworkType;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import org.springframework.stereotype.Repository;

@Jdbc("controllerJdbcTemplate")
@Repository
public class SecurityClusterSubnetDao extends JdbcBaseDaoImpl<SecurityClusterSubnet, Integer> {
    public int deleteByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_subnet where cluster_id = ?";
        Object[] values = {clusterId};
        return this.jdbcTemplate.update(sql, values);
    }

    public int deleteHaByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_subnet where cluster_id = ? and network_type = ?";
        Object[] values = {clusterId, SecurityNetworkType.HA.value()};
        return this.jdbcTemplate.update(sql, values);
    }
}
