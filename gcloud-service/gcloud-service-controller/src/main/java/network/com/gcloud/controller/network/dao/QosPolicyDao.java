package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.QosPolicy;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/30.
 */
@Repository
public class QosPolicyDao extends JdbcBaseDaoImpl<QosPolicy, String> {
}
