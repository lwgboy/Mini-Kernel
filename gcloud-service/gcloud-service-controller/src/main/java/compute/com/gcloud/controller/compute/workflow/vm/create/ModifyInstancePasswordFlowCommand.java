package com.gcloud.controller.compute.workflow.vm.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstancePasswordFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.ModifyPasswordMsg;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class ModifyInstancePasswordFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	private MessageBus bus;
	
	@Override
	protected Object process() throws Exception {
		ModifyInstancePasswordFlowCommandReq req = (ModifyInstancePasswordFlowCommandReq)getReqParams();
		
		ModifyPasswordMsg modifyMsg = new ModifyPasswordMsg();
    	modifyMsg.setInstanceId(req.getInstanceId());
    	modifyMsg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));
    	modifyMsg.setPassword(req.getPassword());
    	modifyMsg.setTaskId(getTaskId());
        bus.send(modifyMsg);
		return null;
	}
	
	public boolean judgeExecute() {
		ModifyInstancePasswordFlowCommandReq req = (ModifyInstancePasswordFlowCommandReq)getReqParams();
		if(StringUtils.isBlank(req.getPassword())) {
			return false;
		}
		return true;
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
		return ModifyInstancePasswordFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
