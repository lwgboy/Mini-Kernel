package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.workflow.model.vm.CheckCreateVmNodeEnvFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.create.CheckCreateVmNodeEnvMsg;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope("prototype")
@Slf4j
public class CheckCreateVmNodeEnvFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	private MessageBus bus;
	
	@Override
	protected Object process() throws Exception {
		CheckCreateVmNodeEnvFlowCommandReq req = (CheckCreateVmNodeEnvFlowCommandReq)getReqParams();
		
		CheckCreateVmNodeEnvMsg msg = new CheckCreateVmNodeEnvMsg();
		msg.setTaskId(getTaskId());
		msg.setInstanceId(req.getInstanceId());
		msg.setUserId(req.getCreateUser().getId());
		
		msg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));
		
		bus.send(msg);
		
//		CheckCreateVmNodeEnvReplyMsg res = (CheckCreateVmNodeEnvReplyMsg)bus.call(msg, CheckCreateVmNodeEnvReplyMsg.class);
		return null;
	}



	@Override
	protected Object rollback() throws Exception {

		CheckCreateVmNodeEnvFlowCommandReq req = (CheckCreateVmNodeEnvFlowCommandReq)getReqParams();

		CleanInstanceFileMsg msg = new CleanInstanceFileMsg();
		msg.setInstanceId(req.getInstanceId());
		msg.setTaskId(getTaskId());
		msg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));

		bus.send(msg);

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		// TODO Auto-generated method stub
		return CheckCreateVmNodeEnvFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
