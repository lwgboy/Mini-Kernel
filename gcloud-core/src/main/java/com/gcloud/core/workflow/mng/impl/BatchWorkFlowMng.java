package com.gcloud.core.workflow.mng.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.core.workflow.dao.IBatchWorkFlowDao;
import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.core.workflow.mng.IBatchWorkFlowMng;
@Service
@Transactional
public class BatchWorkFlowMng implements IBatchWorkFlowMng {
	
	@Autowired
	private IBatchWorkFlowDao batchWorkFlowDao;

	@Override
	public void update(BatchWorkFlow batchFlow, List<String> fields) {
		batchWorkFlowDao.update(batchFlow, fields);
	}

	@Override
	public void delete(BatchWorkFlow batchFlow) {
		batchWorkFlowDao.delete(batchFlow);
	}

	@Override
	public Long save(BatchWorkFlow batchFlow) {
		return batchWorkFlowDao.saveWithIncrementId(batchFlow);
	}

	@Override
	public BatchWorkFlow findById(Long id) {
		return batchWorkFlowDao.getById(id);
	}

	@Override
	public BatchWorkFlow findUnique(String field, String value) {
		return batchWorkFlowDao.findUniqueByProperty(field, value);
	}

	@Override
	public List<BatchWorkFlow> findByProperty(String field, String value) {
		return batchWorkFlowDao.findByProperty(field, value);
	}

}
