package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachNetcardDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachNetcardDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/4.
 */
@Component
@Scope("prototype")
@Slf4j
public class ForceDetachNetcardDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IVmNetworkService vmNetworkService;

    @Override
    protected Object process() throws Exception {
        ForceDetachNetcardDoneFlowCommandReq req = (ForceDetachNetcardDoneFlowCommandReq)getReqParams();

        vmNetworkService.detachDone(req.getInstanceId(), req.getNetcardId(), true);

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
        return ForceDetachNetcardDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceDetachNetcardDoneFlowCommandRes.class;
    }
}
