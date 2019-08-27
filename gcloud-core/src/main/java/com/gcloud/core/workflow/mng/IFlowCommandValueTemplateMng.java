package com.gcloud.core.workflow.mng;

import java.util.List;

import com.gcloud.core.workflow.entity.FlowCommandValueTemplate;

public interface IFlowCommandValueTemplateMng {
	List<FlowCommandValueTemplate> findByProperty(String field,String value);
	List<FlowCommandValueTemplate> getTemplatesByStepId(String flowTypeCode, Integer stepId);
}
