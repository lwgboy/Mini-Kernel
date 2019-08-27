package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.workflow.model.storage.ModifyInstanceSpecWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class ModifyInstanceSpecWorkflow extends BaseWorkFlows {

	@Override
	public String getFlowTypeCode() {
		return "ModifyInstanceSpecWorkflow";
	}

	@Override
	public void process() {

	}

	@Override
	protected Class<?> getReqParamClass() {
		return ModifyInstanceSpecWorkflowReq.class;
	}

	@Override
	public Object preProcess() {
		return null;
	}

}
