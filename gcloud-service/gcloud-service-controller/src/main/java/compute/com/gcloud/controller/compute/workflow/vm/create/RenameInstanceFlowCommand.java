package com.gcloud.controller.compute.workflow.vm.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.workflow.model.vm.ModifyVmHostNameFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.ModifyVmHostNameMsg;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class RenameInstanceFlowCommand extends BaseWorkFlowCommand {
	@Value("${gcloud.controller.vm.mustRename: true}")
	private boolean mustRename;
	
	@Autowired
	private MessageBus bus;

	@Override
	protected Object process() throws Exception {
		ModifyVmHostNameFlowCommandReq req = (ModifyVmHostNameFlowCommandReq)getReqParams();
		
		ModifyVmHostNameMsg modifyMsg = new ModifyVmHostNameMsg();
    	modifyMsg.setInstanceId(req.getInstanceId());
    	modifyMsg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));
    	modifyMsg.setHostName(StringUtils.isBlank(req.getHostName())?req.getInstanceId():req.getHostName());
    	modifyMsg.setTaskId(getTaskId());
        bus.send(modifyMsg);
		
		return null;
	}
	
	@Override
	public boolean judgeExecute() {
		ModifyVmHostNameFlowCommandReq req = (ModifyVmHostNameFlowCommandReq)getReqParams();
		if(StringUtils.isBlank(req.getHostName()) && !mustRename) {
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
		return ModifyVmHostNameFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
