package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.workflow.model.storage.ConfigInstanceResourceFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.ConfigInstanceResourceFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.ConfigInstanceResourceMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class ConfigInstanceResourceFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private MessageBus bus;

	@Autowired
	private InstanceDao instanceDao;

	@Override
	protected Object process() throws Exception {

		ConfigInstanceResourceFlowCommandReq req = (ConfigInstanceResourceFlowCommandReq) getReqParams();

		ConfigInstanceResourceMsg msg = new ConfigInstanceResourceMsg();
		msg.setTaskId(getTaskId());
		msg.setInstanceId(req.getInstanceId());
		msg.setCpu(req.getCpu());
		msg.setMemory(req.getMemory());
		msg.setOrgCpu(req.getOrgCpu());
		msg.setOrgMemory(req.getOrgMemory());
		msg.setServiceId(MessageUtil.computeServiceId(req.getHostName()));

		bus.send(msg);

		return null;
	}

	@Override
	protected Object rollback() throws Exception {

		ConfigInstanceResourceFlowCommandReq req = (ConfigInstanceResourceFlowCommandReq) getReqParams();

		ConfigInstanceResourceMsg msg = new ConfigInstanceResourceMsg();
		msg.setTaskId(getTaskId());
		msg.setInstanceId(req.getInstanceId());
		msg.setCpu(req.getOrgCpu());
		msg.setMemory(req.getOrgMemory());
		msg.setServiceId(MessageUtil.computeServiceId(req.getHostName()));

		bus.send(msg);

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return ConfigInstanceResourceFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return ConfigInstanceResourceFlowCommandRes.class;
	}
}
