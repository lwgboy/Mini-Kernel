package com.gcloud.core.workflow.dao;

import java.util.Date;

import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.framework.db.dao.IJdbcBaseDao;

public interface IBatchWorkFlowDao  extends IJdbcBaseDao<BatchWorkFlow, Long>{
	public void deleteExpireData(String endTime);
}
