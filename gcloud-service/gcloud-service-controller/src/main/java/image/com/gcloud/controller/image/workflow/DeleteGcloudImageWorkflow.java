package com.gcloud.controller.image.workflow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.core.workflow.core.BaseWorkFlows;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class DeleteGcloudImageWorkflow extends BaseWorkFlows{

	@Override
	public String getFlowTypeCode() {
		return "DeleteGcloudImageWorkflow";
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
		return null;
	}

}
