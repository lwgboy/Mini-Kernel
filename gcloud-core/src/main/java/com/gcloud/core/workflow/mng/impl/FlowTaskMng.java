package com.gcloud.core.workflow.mng.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.core.workflow.dao.IBatchWorkFlowDao;
import com.gcloud.core.workflow.dao.IFlowTaskDao;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceDao;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceStepDao;
import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.core.workflow.enums.BatchWorkFlowState;
import com.gcloud.core.workflow.mng.IFlowTaskMng;
@Service
@Transactional
public class FlowTaskMng implements IFlowTaskMng{
	@Autowired
	private IFlowTaskDao flowTaskDao;
	
	@Autowired
	private IBatchWorkFlowDao batchWorkFlowDao;
	
	@Autowired
	private IWorkFlowInstanceDao workFlowInstanceDao;
	
	@Autowired
	private IWorkFlowInstanceStepDao workFlowInstanceStepDao;
	
	@Override
	public void update(FlowTask task, List<String> fields) {
		flowTaskDao.update(task, fields);
	}

	@Override
	public Long save(FlowTask task) {
		return flowTaskDao.saveWithIncrementId(task);
	}

	@Override
	public FlowTask findById(Long id) {
		return flowTaskDao.getById(id);
	}

	@Override
	public FlowTask findUnique(String field, String value) {
		return flowTaskDao.findUniqueByProperty(field, value);
	}

	@Override
	public List<WorkFlowInstance> getSubFlows(Long flowId, int stepId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("parentFlowId", flowId);
		filters.put("parentFlowStepId", stepId);
		FlowTask task = flowTaskDao.findUniqueByProperties(filters);
		
		List<BatchWorkFlow> batchs = batchWorkFlowDao.findByProperty("ptaskId", task.getTaskId());
		List<Long> successFlowIds = batchs.stream().filter(batch -> batch.getState().equals(BatchWorkFlowState.SUCCESS.name())).map(BatchWorkFlow::getFlowId).collect(Collectors.toList());
		
		return workFlowInstanceDao.getFlowsByIds(successFlowIds);
	}

	@Override
	public void deleteWorkflowExpireData(String endTime) {
		flowTaskDao.deleteExpireData(endTime);
		batchWorkFlowDao.deleteExpireData(endTime);
		workFlowInstanceDao.deleteExpireData(endTime);
		workFlowInstanceStepDao.deleteExpireData(endTime);
	}

}
