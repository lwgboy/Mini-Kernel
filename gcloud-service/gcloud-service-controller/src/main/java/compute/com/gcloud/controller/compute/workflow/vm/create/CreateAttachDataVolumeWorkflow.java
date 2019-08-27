package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.workflow.model.vm.CreateAttachDataVolumeWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateAttachDataVolumeWorkflow  extends BaseWorkFlows{

	@Override
	public String getFlowTypeCode() {
		return "CreateAttachDataVolumeWorkflow";
	}

	@Override
	public Object preProcess() {
		return null;
	}

	@Override
	public void process() {
		
	}

	@Override
	public boolean judgeExecute() {
		CreateAttachDataVolumeWorkflowReq req = (CreateAttachDataVolumeWorkflowReq)getReqParams();
		return req.getRepeatParams() != null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CreateAttachDataVolumeWorkflowReq.class;
	}


}
