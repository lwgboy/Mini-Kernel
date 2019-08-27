package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;
import com.gcloud.controller.security.model.workflow.CreateAndAttachHaNetcardInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateAndAttachHaNetcardInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateAndAttachHaNetcardInitFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {

        CreateAndAttachHaNetcardInitFlowCommandReq req = (CreateAndAttachHaNetcardInitFlowCommandReq)getReqParams();
        CreateAndAttachHaNetcardInitFlowCommandRes res = new CreateAndAttachHaNetcardInitFlowCommandRes();
        res.setInstanceId(req.getHaNetcardInfo().getObjectId());
        ClusterCreateNetcardInfo netcardInfo = req.getHaNetcardInfo().getNetcardInfo();
        res.setOvsBridgeId(netcardInfo.getClusterOvsId());
        res.setSecurityGroupId(netcardInfo.getSecurityGroupId());
        res.setSubnetId(netcardInfo.getSubnetId());

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
        return CreateAndAttachHaNetcardInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateAndAttachHaNetcardInitFlowCommandRes.class;
    }
}
