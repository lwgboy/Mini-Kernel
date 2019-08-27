package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.senior.IVmSeniorService;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstanceSpecInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.ModifyInstanceSpecInitFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.InstanceDomainDetailMsg;
import com.gcloud.header.compute.msg.node.vm.base.InstanceDomainDetailReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yaowj on 2018/11/20.
 */
@Component
@Scope("prototype")
@Slf4j
public class ModifyInstanceSpecInitFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private InstanceDao instanceDao;

	@Autowired
	private InstanceTypeDao instanceTypeDao;

	@Autowired
	private IVmSeniorService vmSeniorService;

	@Autowired
	private MessageBus bus;

	@Override
	protected Object process() throws Exception {
		ModifyInstanceSpecInitFlowCommandReq req = (ModifyInstanceSpecInitFlowCommandReq) getReqParams();
		vmSeniorService.modifyInstanceInit(req.getInstanceId(), req.getInstanceType(), false);
		ModifyInstanceSpecInitFlowCommandRes res = new ModifyInstanceSpecInitFlowCommandRes();
		try {
			VmInstance instance = instanceDao.getById(req.getInstanceId());
			InstanceType instanceType = instanceTypeDao.getById(req.getInstanceType());

			res.setHostName(instance.getHostname());
			res.setCpu(instanceType.getVcpus());
			res.setMemory(instanceType.getMemoryMb());
			res.setTaskId(UUID.randomUUID().toString());
			res.setOrgCpu(instance.getCore());
			res.setOrgMemory(instance.getMemory());
		} catch (Exception ex) {
			instanceDao.cleanState(req.getInstanceId());
			throw ex;
		}
		return res;
	}

	@Override
	protected Object rollback() throws Exception {

		errorRollback();

		return null;
	}

	private void errorRollback() {
		ModifyInstanceSpecInitFlowCommandReq req = (ModifyInstanceSpecInitFlowCommandReq) getReqParams();
		VmInstance instance = instanceDao.getById(req.getInstanceId());
		List<String> updateField = new ArrayList<>();
		try{
			InstanceDomainDetailMsg msg = new InstanceDomainDetailMsg();
			msg.setServiceId(MessageUtil.computeServiceId(instance.getHostname()));
			msg.setInstanceId(instance.getId());

			InstanceDomainDetailReplyMsg reply = bus.call(msg, InstanceDomainDetailReplyMsg.class);

			//如果实际已经改了，也修改状态
			InstanceType newInstanceType = instanceTypeDao.getById(req.getInstanceType());
			if(newInstanceType != null && newInstanceType.getVcpus().equals(reply.getVcpu()) && newInstanceType.getMemoryMb().equals(reply.getMemory())){
				updateField.add(instance.updateCore(reply.getVcpu()));
				updateField.add(instance.updateMemory(reply.getCurrentMemory()));
				updateField.add(instance.updateInstanceType(req.getInstanceType()));
			}

		}catch (Exception ex){
			log.error(String.format("获取虚拟机信息失败，instanceId=%s, ex=%s", req.getInstanceId(), ex), ex);
		}

		updateField.add(instance.updateStepState(null));
		updateField.add(instance.updateTaskState(null));

		instanceDao.update(instance, updateField);

	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return ModifyInstanceSpecInitFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return ModifyInstanceSpecInitFlowCommandRes.class;
	}
}
