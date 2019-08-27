package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.workflow.model.vm.CreateDomainFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceIfExistMsg;
import com.gcloud.header.compute.msg.node.vm.create.CreateDomainMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateDomainFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private MessageBus bus;

	@Override
	protected Object process() throws Exception {
		CreateDomainFlowCommandReq req = (CreateDomainFlowCommandReq)getReqParams();

		CreateDomainMsg msg = new CreateDomainMsg();
		msg.setTaskId(getTaskId());
		msg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));
		msg.setInstanceId(req.getInstanceId());
		msg.setUserId(req.getCreateUser().getId());
		bus.send(msg);
		
		return null;
	}

	/*@Override
	protected Object postProcess(Map<String, Object> params) throws Exception {
		// 根据执行结果修改数据库  
		return null;
	}*/

	@Override
	protected Object rollback() throws Exception {

		CreateDomainFlowCommandReq req = (CreateDomainFlowCommandReq)getReqParams();

		DestroyInstanceIfExistMsg msg = new DestroyInstanceIfExistMsg();
		msg.setInstanceId(req.getInstanceId());
		msg.setTaskId(this.getTaskId());
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
		return CreateDomainFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
