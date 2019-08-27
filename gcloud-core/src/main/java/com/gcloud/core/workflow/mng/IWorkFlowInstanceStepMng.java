package com.gcloud.core.workflow.mng;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;

public interface IWorkFlowInstanceStepMng {
	void update(WorkFlowInstanceStep step, List<String> fields);
	Long save(WorkFlowInstanceStep step);
	WorkFlowInstanceStep findById(Long id);
	WorkFlowInstanceStep findUnique(String field,String value);
	WorkFlowInstanceStep findByTemplateStepId(Integer templateStepId, Long flowId);
	List<WorkFlowInstanceStep> getStepsByIds(Long flowId, String toIds);
	WorkFlowInstanceStep getFirstStep(Long flowId);
	List<WorkFlowInstanceStep> getNextRollBackSteps(Long flowId, boolean rollbackFailContinue);
	boolean isStepsDone(Long flowId, String ids);//EXECUTING、ROLLBACKING 没有这两个中间状态即为true
	WorkFlowInstanceStep getLastedSuccessStep(Long flowId);
	List<WorkFlowInstanceStep> getAllNotFinishedStep();
	
	public List<String> getFirstStepRess(Long flowTaskId);
	
	public List<String> getLastStepRess(Long flowTaskId);
	
	JSONArray getRepeatStepRes(Long flowId,Integer stepId);
}
