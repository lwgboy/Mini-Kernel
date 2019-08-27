package com.gcloud.controller.compute.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.compute.entity.ZoneNodeEntity;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;

@Jdbc("controllerJdbcTemplate")
@Repository
public class ZoneNodeDao extends JdbcBaseDaoImpl<ZoneNodeEntity, String>{
}
