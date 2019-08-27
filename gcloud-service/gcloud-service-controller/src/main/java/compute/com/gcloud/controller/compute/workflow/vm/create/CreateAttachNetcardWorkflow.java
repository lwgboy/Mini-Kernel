package com.gcloud.controller.compute.workflow.vm.create;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.compute.workflow.model.vm.CreateAttachNetcardWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;

import lombok.extern.slf4j.Slf4j;
@Component
@Scope("prototype")
@Slf4j
public class CreateAttachNetcardWorkflow extends BaseWorkFlows {

	@Override
	public String getFlowTypeCode() {
		return "CreateAttachNetcardWorkflow";
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
		return CreateAttachNetcardWorkflowReq.class;
	}
	
	@Override
	public boolean judgeExecute() {
		CreateAttachNetcardWorkflowReq req = (CreateAttachNetcardWorkflowReq)getReqParams();
		return (null != req.getSubnetId());
	}

}
