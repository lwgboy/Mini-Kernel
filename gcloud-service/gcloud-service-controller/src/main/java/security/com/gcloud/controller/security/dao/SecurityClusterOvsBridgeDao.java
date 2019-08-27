package com.gcloud.controller.security.dao;

import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import org.springframework.stereotype.Repository;

@Jdbc("controllerJdbcTemplate")
@Repository
public class SecurityClusterOvsBridgeDao extends JdbcBaseDaoImpl<SecurityClusterOvsBridge, String> {

    public int deleteByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_ovsbridge where cluster_id = ?";
        Object[] values = {clusterId};
        return this.jdbcTemplate.update(sql, values);
    }

    public int deleteHaByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_ovsbridge where cluster_id = ? and ha = true";
        Object[] values = {clusterId};
        return this.jdbcTemplate.update(sql, values);
    }
}
