package com.gcloud.core.workflow.mng;

import java.util.List;

import com.gcloud.core.workflow.entity.WorkFlowTemplate;

public interface IWorkFlowTemplateMng {
	WorkFlowTemplate findById(Integer id);
	WorkFlowTemplate findUnique(String field,String value);
	List<WorkFlowTemplate> findByProperty(String field,String value);
}
