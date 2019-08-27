package com.gcloud.controller.compute.workflow.vm.storage;


import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DetachDataDiskWorkflow extends BaseWorkFlows {

	@Override
	public String getFlowTypeCode() {
		return "DetachDataDiskWorkflow";
	}

	@Override
	public void process() {




	}

	@Override
	protected Class<?> getReqParamClass() {
		return DetachDataDiskWorkflowReq.class;
	}

	@Override
	public Object preProcess() {
		return null;
	}

}
