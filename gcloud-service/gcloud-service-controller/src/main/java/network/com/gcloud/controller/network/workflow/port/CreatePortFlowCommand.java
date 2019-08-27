package com.gcloud.controller.network.workflow.port;

import com.gcloud.controller.compute.workflow.model.vm.CreateNetcardFlowCommandRes;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.model.workflow.CreatePortFlowCommandReq;
import com.gcloud.controller.network.model.workflow.CreatePortFlowCommandRes;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/12.
 */
@Component
@Scope("prototype")
@Slf4j
public class CreatePortFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IPortService portService;


    @Override
    protected Object process() throws Exception {

        CreatePortFlowCommandReq req = (CreatePortFlowCommandReq)getReqParams();

        CreatePortParams params = new CreatePortParams();
        params.setSecurityGroupId(req.getSecurityGroupId());
        params.setIpAddress(req.getIpAddress());
        params.setSubnetId(req.getSubnetId());

        String portId = portService.create(params, req.getCreateUser());

        CreatePortFlowCommandRes res = new CreatePortFlowCommandRes();
        res.setPortId(portId);

        return res;
    }

    @Override
    protected Object rollback() throws Exception {

        CreatePortFlowCommandRes res = (CreatePortFlowCommandRes)getResParams();

        portService.delete(res.getPortId());

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
        return CreatePortFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreatePortFlowCommandRes.class;
    }
}
