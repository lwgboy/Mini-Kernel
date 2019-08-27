package com.gcloud.core.workflow.dao;

import java.util.Date;

import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.framework.db.dao.IJdbcBaseDao;

public interface IFlowTaskDao extends IJdbcBaseDao<FlowTask, Long>{
	public void deleteExpireData(String endTime);
}
