package com.gcloud.controller.storage.workflow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.workflow.model.volume.DeleteDiskWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class DeleteDiskWorkflow extends BaseWorkFlows{
	@Override
	public String getFlowTypeCode() {
		return "DeleteDiskWorkflow";
	}

	@Override
	public Object preProcess() {
		return null;
	}

	@Override
	public void process() {
		
	}

	@Override
	protected Class<?> getReqParamClass() {
		return DeleteDiskWorkflowReq.class;
	}

}
