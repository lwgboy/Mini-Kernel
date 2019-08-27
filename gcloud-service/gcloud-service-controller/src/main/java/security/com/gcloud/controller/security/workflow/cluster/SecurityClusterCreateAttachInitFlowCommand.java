package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.model.workflow.SecurityClusterCreateAttachInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.SecurityClusterCreateAttachInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class SecurityClusterCreateAttachInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterOvsBridgeDao clusterOvsBridgeDao;

    @Override
    protected Object process() throws Exception {

        SecurityClusterCreateAttachInitFlowCommandReq req = (SecurityClusterCreateAttachInitFlowCommandReq)getReqParams();
        SecurityClusterCreateAttachInitFlowCommandRes res = new SecurityClusterCreateAttachInitFlowCommandRes();
        res.setInstanceId(req.getInstanceId());
        if(StringUtils.isNotBlank(req.getNetcardInfo().getClusterOvsId())){
            SecurityClusterOvsBridge ovsBridge = clusterOvsBridgeDao.getById(req.getNetcardInfo().getClusterOvsId());
            res.setOvsBridgeId(ovsBridge.getOvsBridgeId());
        }
        res.setSecurityGroupId(req.getNetcardInfo().getSecurityGroupId());
        res.setSubnetId(req.getNetcardInfo().getSubnetId());
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
        return SecurityClusterCreateAttachInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return SecurityClusterCreateAttachInitFlowCommandRes.class;
    }
}
