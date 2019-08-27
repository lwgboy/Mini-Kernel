package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.workflow.model.network.DetachPortDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.DetachPortDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
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
public class DetachPortDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IVmNetworkService networkService;

    @Override
    protected Object process() throws Exception {
        DetachPortDoneFlowCommandReq req = (DetachPortDoneFlowCommandReq)getReqParams();
        networkService.detachDone(req.getInstanceId(), req.getNetworkDetail().getPortId(), req.getInTask());
        return null;
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
        return DetachPortDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DetachPortDoneFlowCommandRes.class;
    }
}
