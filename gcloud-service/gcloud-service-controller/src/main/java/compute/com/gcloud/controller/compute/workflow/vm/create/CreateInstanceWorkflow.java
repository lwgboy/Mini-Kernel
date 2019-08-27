package com.gcloud.controller.compute.workflow.vm.create;


import com.gcloud.controller.compute.service.vm.create.IVmCreateService;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CreateInstanceWorkflow extends BaseWorkFlows {

	@Autowired
	private IVmCreateService vmCreateService;

	@Override
	public String getFlowTypeCode() {
		return "CreateInstanceWorkflow";
	}

	@Override
	public void process() {

	}

	@Override
    protected Class<?> getReqParamClass() {
        return CreateInstanceWorkflowReq.class;
    }

	@Override
	public Object preProcess() {
		return null;
	}
}
