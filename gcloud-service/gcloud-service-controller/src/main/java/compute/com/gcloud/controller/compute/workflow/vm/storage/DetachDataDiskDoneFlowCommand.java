package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DetachDataDiskDoneFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private IVmBaseService vmBaseService;

	@Override
	protected Object process() throws Exception {
		DetachDataDiskDoneFlowCommandReq req = (DetachDataDiskDoneFlowCommandReq) getReqParams();

		vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

		return null;
	}

	@Override
	protected Object rollback() throws Exception {
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return DetachDataDiskDoneFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return DetachDataDiskDoneFlowCommandRes.class;
	}
}
