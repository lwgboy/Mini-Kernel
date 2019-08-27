package com.gcloud.controller.security.dao;

import com.gcloud.controller.security.entity.SecurityClusterNetworkConfig;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import org.springframework.stereotype.Repository;

@Jdbc("controllerJdbcTemplate")
@Repository
public class SecurityClusterNetworkConfigDao extends JdbcBaseDaoImpl<SecurityClusterNetworkConfig, String> {

}
