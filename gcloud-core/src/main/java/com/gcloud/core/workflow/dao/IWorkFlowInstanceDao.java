package com.gcloud.core.workflow.dao;

import java.util.Date;
import java.util.List;

import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.framework.db.dao.IJdbcBaseDao;

public interface IWorkFlowInstanceDao extends IJdbcBaseDao<WorkFlowInstance, Long>{
	List<WorkFlowInstance> getFlowsByIds(List<Long> ids);
	public void deleteExpireData(String endTime);
}
