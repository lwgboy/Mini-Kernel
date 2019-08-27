package com.gcloud.core.workflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.core.workflow.dao.IBatchWorkFlowDao;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceDao;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceStepDao;
import com.gcloud.core.workflow.service.IWorkflowService;

public class WorkflowServiceImpl implements IWorkflowService{
	@Autowired
	private IWorkFlowInstanceDao workFlowInstanceDao;
	
	@Autowired
	private IWorkFlowInstanceStepDao workFlowInstanceStepDao;
	
	@Autowired
	private IBatchWorkFlowDao batchWorkFlowDao;
}
