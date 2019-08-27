package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstanceSpecDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstanceSpecDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class ModifyInstanceSpecDoneFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private InstanceDao instanceDao;

	@Override
	protected Object process() throws Exception {
		ModifyInstanceSpecDoneFlowCommandReq req = (ModifyInstanceSpecDoneFlowCommandReq) getReqParams();
		List<String> updateField = new ArrayList<>();
		VmInstance instance = new VmInstance();
		instance.setId(req.getInstanceId());

		updateField.add(instance.updateInstanceType(req.getInstanceType()));
		updateField.add(instance.updateCore(req.getCpu()));
		updateField.add(instance.updateMemory(req.getMemory()));
		updateField.add(instance.updateTaskState(null));
		updateField.add(instance.updateStepState(null));

		instanceDao.update(instance, updateField);

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
		return ModifyInstanceSpecDoneFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return ModifyInstanceSpecDoneFlowCommandRes.class;
	}
}
