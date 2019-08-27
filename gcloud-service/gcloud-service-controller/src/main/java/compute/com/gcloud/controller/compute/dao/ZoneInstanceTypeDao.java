package com.gcloud.controller.compute.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.compute.entity.ZoneInstanceTypeEntity;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;

@Jdbc("controllerJdbcTemplate")
@Repository
public class ZoneInstanceTypeDao extends JdbcBaseDaoImpl<ZoneInstanceTypeEntity, String>{

}
