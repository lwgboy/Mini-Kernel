package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowDoneCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowDoneCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.VmState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class CreateInstanceFlowDoneCommand  extends BaseWorkFlowCommand{
	@Autowired
	private InstanceDao vmInstanceDao;

	@Autowired
	private IVmBaseService vmBaseService;

	@Override
	protected Object process() throws Exception {
		CreateInstanceFlowDoneCommandReq req = (CreateInstanceFlowDoneCommandReq) getReqParams();
		VmInstance vmIns = vmInstanceDao.getById(req.getInstanceId());
		vmIns.setStartTime(new Date());
		vmIns.setState(VmState.RUNNING.value());
		List<String> updateFields = new ArrayList<String>();
		updateFields.add(VmInstance.START_TIME);
		updateFields.add(VmInstance.STATE);

		
		vmInstanceDao.update(vmIns, updateFields);
        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

		CreateInstanceFlowDoneCommandRes res = new CreateInstanceFlowDoneCommandRes();
		res.setInstanceId(req.getInstanceId());
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		CreateInstanceFlowDoneCommandReq req = (CreateInstanceFlowDoneCommandReq) getReqParams();
		VmInstance vmIns = vmInstanceDao.getById(req.getInstanceId());
		if(vmIns != null && vmIns.getState().equals(VmState.RUNNING.value())) {
			return true;
		}
		return false;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CreateInstanceFlowDoneCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
