package com.gcloud.controller.compute.workflow.vm.senior.bundle;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/30.
 */
@Component
@Scope("prototype")
@Slf4j
public class BundleInstanceDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IVmBaseService vmBaseService;

    @Override
    protected Object process() throws Exception {

        BundleInstanceDoneFlowCommandReq req = (BundleInstanceDoneFlowCommandReq)getReqParams();
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
        return BundleInstanceDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return BundleInstanceDoneFlowCommandRes.class;
    }
}
