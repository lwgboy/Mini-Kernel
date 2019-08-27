package com.gcloud.core.workflow.mng.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.core.workflow.dao.IWorkFlowInstanceDao;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceMng;
@Service
@Transactional
public class WorkFlowInstanceMng implements IWorkFlowInstanceMng{
	@Autowired
	private IWorkFlowInstanceDao workFlowInstanceDao;

	@Override
	public void update(WorkFlowInstance ins, List<String> fields) {
		workFlowInstanceDao.update(ins, fields);
	}

	@Override
	public Long save(WorkFlowInstance ins) {
		return workFlowInstanceDao.saveWithIncrementId(ins);
	}

	@Override
	public WorkFlowInstance findById(Long id) {
		return workFlowInstanceDao.getById(id);
	}

	@Override
	public WorkFlowInstance findUnique(String field, String value) {
		return workFlowInstanceDao.findUniqueByProperty(field, value);
	}

	@Override
	public WorkFlowInstance getSubFlow(Long flowId, int stepId) {
		Map<String,Object> search = new HashMap<String,Object>();
		search.put("parentFlowId", flowId);
		search.put("parentFlowStepId", stepId);
		return workFlowInstanceDao.findUniqueByProperties(search);
	}

}
