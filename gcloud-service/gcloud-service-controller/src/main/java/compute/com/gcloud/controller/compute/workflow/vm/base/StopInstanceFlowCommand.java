package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.vm.StopInstanceCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.StopInstanceCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StopInstanceMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StopInstanceFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

	@Autowired
	private MessageBus bus;

	@Override
	protected Object process() throws Exception {
		StopInstanceCommandReq req = (StopInstanceCommandReq)getReqParams();

        VmInstance vm = instanceDao.getById(req.getInstanceId());

        StopInstanceMsg msg = new StopInstanceMsg();
        msg.setTaskId(this.getTaskId());
		msg.setInstanceId(vm.getId());
		msg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));

        StopInstanceCommandRes res = new StopInstanceCommandRes();
        res.setLastState(vm.getLastState());

		bus.send(msg);

		return res;
	}

	@Override
	protected Object rollback() throws Exception {
        StopInstanceCommandReq req = (StopInstanceCommandReq)getReqParams();
        StopInstanceCommandRes res = (StopInstanceCommandRes)getResParams();

		VmInstance vm = instanceDao.getById(req.getInstanceId());
		StartInstanceMsg msg = new StartInstanceMsg();
		msg.setTaskId(this.getTaskId());
		msg.setInstanceId(req.getInstanceId());
		msg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));

		bus.send(msg);

		//开机逻辑
		return null;
	}

	@Override
	protected Object timeout() throws Exception {

		return null;
	}

	@Override
	public boolean judgeExecute() {
		StopInstanceCommandReq req = (StopInstanceCommandReq)getReqParams();
		VmInstance vm = instanceDao.getById(req.getInstanceId());
		if(VmState.RUNNING.value().equals(vm.getState())){
			return true;
		}
		return false;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return StopInstanceCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return StopInstanceCommandRes.class;
	}

}
