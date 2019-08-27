package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.workflow.DetachAndDeleteHaNetcardInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DetachAndDeleteHaNetcardInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DetachAndDeleteHaNetcardInitFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {

        DetachAndDeleteHaNetcardInitFlowCommandReq req = (DetachAndDeleteHaNetcardInitFlowCommandReq)getReqParams();

        DetachAndDeleteHaNetcardInitFlowCommandRes res = new DetachAndDeleteHaNetcardInitFlowCommandRes();
        res.setInstanceId(req.getHaNetcardInfo().getObjectId());
        res.setNetworkInterfaceId(req.getHaNetcardInfo().getNetcardId());

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
        return DetachAndDeleteHaNetcardInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DetachAndDeleteHaNetcardInitFlowCommandRes.class;
    }
}
