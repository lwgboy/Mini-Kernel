package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.vm.ConnectDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.ConnectDiskFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class ConnectDiskFlowCommand extends BaseWorkFlowCommand {

	@Override
	protected Object process() throws Exception {
		ConnectDiskFlowCommandReq req = (ConnectDiskFlowCommandReq) getReqParams();
		log.debug(String.format("虚拟机%s 连接到物理机%s, 开始", req.getVolumeId(), req.getCreateHost()));

		//IVolumeService volumeService = (IVolumeService) SpringUtil.getBean("volumeServiceImpl");
		//ConnectionInfo connectionInfo = volumeService.initializeConnection(req.getVolumeId(), req.getHostName());

		log.debug(String.format("虚拟机%s 连接到物理机%s, 结束", req.getVolumeId(), req.getCreateHost()));

		ConnectDiskFlowCommandRes res = new ConnectDiskFlowCommandRes();

		return res;
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
		return ConnectDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return ConnectDiskFlowCommandRes.class;// AttachVolumeFlowCommandRes.class;
	}
	
	@Override
	public boolean judgeExecute() {
		//暂时没有iscsi，暂时取消
		return false;
	}

}
