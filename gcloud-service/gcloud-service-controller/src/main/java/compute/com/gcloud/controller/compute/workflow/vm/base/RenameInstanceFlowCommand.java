package com.gcloud.controller.compute.workflow.vm.base;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.compute.workflow.model.vm.ModifyVmHostNameFlowCommandReq;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class RenameInstanceFlowCommand extends BaseWorkFlowCommand {
	@Override
	protected Object process() throws Exception {
		return null;
	}

	@Override
	protected Object rollback() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return ModifyVmHostNameFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
