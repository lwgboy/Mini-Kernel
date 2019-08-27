package com.gcloud.controller.compute.dao;

import com.gcloud.controller.compute.entity.ComputeNodeConnectLog;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/19.
 */
@Jdbc("controllerJdbcTemplate")
@Repository
public class ComputeNodeConnectLogDao extends JdbcBaseDaoImpl<ComputeNodeConnectLog, Integer> {
}
