package com.gcloud.controller.network.workflow.port;

import com.gcloud.controller.network.model.workflow.DeleteNetcardFlowCommandReq;
import com.gcloud.controller.network.model.workflow.DeleteNetcardFlowCommandRes;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
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
public class DeleteNetcardFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IPortService portService;

    @Override
    protected Object process() throws Exception {
        DeleteNetcardFlowCommandReq req = (DeleteNetcardFlowCommandReq) getReqParams();
        portService.delete(req.getNetcardId());

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
    public boolean judgeExecute() {
        DeleteNetcardFlowCommandReq req = (DeleteNetcardFlowCommandReq) getReqParams();
        return req == null || req.isDelete();
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteNetcardFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteNetcardFlowCommandRes.class;
    }
}
