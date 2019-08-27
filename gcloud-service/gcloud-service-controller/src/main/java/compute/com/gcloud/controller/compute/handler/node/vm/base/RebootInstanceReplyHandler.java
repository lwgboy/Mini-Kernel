package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.RebootInstanceReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Handler
public class RebootInstanceReplyHandler extends AsyncMessageHandler<RebootInstanceReplyMsg>{

	@Autowired
	private InstanceDao vmInstanceDao;


	@Override
	public void handle(RebootInstanceReplyMsg msg) {
		VmInstance vm = new VmInstance();
		vm.setId(msg.getInstanceId());
		List<String> fields = new ArrayList<>();
		fields.add(vm.updateStepState(null));
		if(msg.getSuccess()){
			fields.add(vm.updateState(VmState.RUNNING.value()));
			vmInstanceDao.update(vm, fields);
		}else{
			fields.add(vm.updateState(msg.getCurrentState()));
			vmInstanceDao.update(vm, fields);
		}

	}
}
