package com.gcloud.core.workflow.mng.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.core.workflow.dao.impl.WorkFlowTypeDao;
import com.gcloud.core.workflow.entity.WorkFlowType;
import com.gcloud.core.workflow.mng.IWorkFlowTypeMng;
@Service
public class WorkFlowTypeMng implements IWorkFlowTypeMng {
	
	@Autowired
	WorkFlowTypeDao workFlowTypeDao;
	
	public WorkFlowType findUniqueByCode(String code) {
		return workFlowTypeDao.findUniqueByProperty("flowTypeCode", code);
	}
}
