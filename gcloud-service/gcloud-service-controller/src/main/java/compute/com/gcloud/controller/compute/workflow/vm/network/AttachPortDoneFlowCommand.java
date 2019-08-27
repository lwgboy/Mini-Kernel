package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.workflow.model.network.AttachPortDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.AttachPortDoneFlowCommandRes;
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
public class AttachPortDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IVmBaseService vmBaseService;

    @Override
    protected Object process() throws Exception {
        AttachPortDoneFlowCommandReq req = (AttachPortDoneFlowCommandReq)getReqParams();

        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

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
        return AttachPortDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return AttachPortDoneFlowCommandRes.class;
    }
}
