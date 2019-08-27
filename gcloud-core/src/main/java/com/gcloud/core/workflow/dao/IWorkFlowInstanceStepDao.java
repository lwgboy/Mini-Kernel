package com.gcloud.core.workflow.dao;

import java.util.Date;
import java.util.List;

import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.framework.db.dao.IJdbcBaseDao;

public interface IWorkFlowInstanceStepDao  extends IJdbcBaseDao<WorkFlowInstanceStep, Long>{
	public List<WorkFlowInstanceStep> getStepsByIds(Long flowId, String toIds);

	public List<WorkFlowInstanceStep> getNextRollBackSteps(Long flowId, boolean rollbackFailContinue);
	
	public WorkFlowInstanceStep getLastedSuccessStep(Long flowId);
	
	public List<WorkFlowInstanceStep> getAllNotFinishedStep();
	
	public List<String> getFirstStepRess(Long flowTaskId);
	
	public List<String> getLastStepRess(Long flowTaskId);
	
	public List<WorkFlowInstanceStep> getStepsByTaskId(String taskId);
	
	public List<WorkFlowInstanceStep> getRepeatSteps(Long flowId,Integer stepId);
	
	public WorkFlowInstanceStep getFirstStep(Long flowId);
	
	public void deleteExpireData(String endTime);
}
