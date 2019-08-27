package com.gcloud.core.workflow.mng;

import java.util.List;

import com.gcloud.core.workflow.entity.WorkFlowInstance;

public interface IWorkFlowInstanceMng {
	void update(WorkFlowInstance ins, List<String> fields);
	Long save(WorkFlowInstance ins);
	WorkFlowInstance findById(Long id);
	WorkFlowInstance findUnique(String field,String value);
	WorkFlowInstance getSubFlow(Long flowId, int stepId);
}
