package com.gcloud.core.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.dao.IWorkFlowTemplateDao;
import com.gcloud.core.workflow.entity.WorkFlowTemplate;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class WorkFlowTemplateDao extends JdbcBaseDaoImpl<WorkFlowTemplate, Integer> implements IWorkFlowTemplateDao{

}
