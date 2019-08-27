package com.gcloud.core.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.entity.WorkFlowType;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class WorkFlowTypeDao extends JdbcBaseDaoImpl<WorkFlowType, Integer> {

}
