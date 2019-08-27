package com.gcloud.controller.compute.workflow.vm.network;


import com.gcloud.controller.compute.workflow.model.network.AttachPortWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class AttachPortWorkflow extends BaseWorkFlows {

	@Override
	public String getFlowTypeCode() {
		return "AttachPortWorkflow";
	}

	@Override
	public void process() {


	}

	@Override
	protected Class<?> getReqParamClass() {
		return AttachPortWorkflowReq.class;
	}

	@Override
	public Object preProcess() {
		return null;
	}

}
