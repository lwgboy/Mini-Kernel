package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.workflow.model.vm.JudgeNeedStartupFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.JudgeNeedStartupFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.VmState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/30.
 */
@Component
@Scope("prototype")
@Slf4j
public class JudgeNeedStartupFlowCommand extends BaseWorkFlowCommand {
    @Override
    protected Object process() throws Exception {

        JudgeNeedStartupFlowCommandReq req = (JudgeNeedStartupFlowCommandReq)getReqParams();
        if(!VmState.RUNNING.value().equals(req.getBeginningState())){
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
        return JudgeNeedStartupFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return JudgeNeedStartupFlowCommandRes.class;
    }
}
