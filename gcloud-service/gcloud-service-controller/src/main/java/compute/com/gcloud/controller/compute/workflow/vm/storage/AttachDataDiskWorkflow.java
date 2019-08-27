package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class AttachDataDiskWorkflow extends BaseWorkFlows {

    @Autowired
    private IVmDiskService vmDiskService;

	@Override
	public String getFlowTypeCode() {
		return "AttachDataDiskWorkflow";
	}

	@Override
	public void process() {

	}

	@Override
	protected Class<?> getReqParamClass() {
		return AttachDataDiskWorkflowReq.class;
	}

	@Override
	public Object preProcess() {
		return null;
	}

}
