package com.gcloud.core.workflow.mng;

import java.util.Date;
import java.util.List;

import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.core.workflow.entity.WorkFlowInstance;

public interface IFlowTaskMng {
	void update(FlowTask task, List<String> fields);
	Long save(FlowTask task);
	FlowTask findById(Long id);
	FlowTask findUnique(String field,String value);
	List<WorkFlowInstance> getSubFlows(Long flowId, int stepId);
	void deleteWorkflowExpireData(String endTime);
}
