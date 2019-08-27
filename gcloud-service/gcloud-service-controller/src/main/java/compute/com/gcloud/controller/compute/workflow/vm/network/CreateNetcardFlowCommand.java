package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.workflow.model.vm.CreateNetcardFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CreateNetcardFlowCommandRes;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 该command没有用到
 *
 */
@Component
@Scope("prototype")
@Slf4j
public class CreateNetcardFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	IPortService portService;
	
	@Override
	protected Object process() throws Exception {
		CreateNetcardFlowCommandReq req = (CreateNetcardFlowCommandReq)getReqParams();
		
		CreatePortParams params = new CreatePortParams();
        params.setSubnetId(req.getvSwitchId());
        params.setName(req.getInstanceId() + "_net");
        params.setIpAddress(req.getPrivateIpAddress());
        params.setSecurityGroupId(req.getSecurityGroupId());
		
		CreateNetcardFlowCommandRes res = new CreateNetcardFlowCommandRes();
		res.setNetcardId(portService.create(params, null));
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		CreateNetcardFlowCommandRes res = (CreateNetcardFlowCommandRes)getResParams();
		// 删除网卡
		portService.delete(res.getNetcardId());
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		CreateNetcardFlowCommandRes res = (CreateNetcardFlowCommandRes)getResParams();
		VmNetworkDetail vmNet = portService.getNetworkDetail(res.getNetcardId());
		if(null != vmNet) {
			return true;
		}
		return false;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CreateNetcardFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return CreateNetcardFlowCommandRes.class;
	}

}
