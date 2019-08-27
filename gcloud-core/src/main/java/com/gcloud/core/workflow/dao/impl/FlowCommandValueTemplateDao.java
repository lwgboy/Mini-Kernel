package com.gcloud.core.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.dao.IFlowCommandValueTemplateDao;
import com.gcloud.core.workflow.entity.FlowCommandValueTemplate;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class FlowCommandValueTemplateDao extends JdbcBaseDaoImpl<FlowCommandValueTemplate, Integer> implements IFlowCommandValueTemplateDao{

}
