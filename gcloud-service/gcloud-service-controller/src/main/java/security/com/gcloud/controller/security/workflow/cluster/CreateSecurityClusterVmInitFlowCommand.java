package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.model.ClusterCreateVmInfo;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterVmInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterVmInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterVmInitFlowCommand extends BaseWorkFlowCommand {

    @Override
    protected Object process() throws Exception {
        CreateSecurityClusterVmInitFlowCommandReq req = (CreateSecurityClusterVmInitFlowCommandReq)getReqParams();
        CreateClusterInfoParams clusterInfo = req.getComponent().getCreateInfo();
        ClusterCreateVmInfo vmInfo = req.getComponent().getCreateVm();

        CreateSecurityClusterVmInitFlowCommandRes res = new CreateSecurityClusterVmInitFlowCommandRes();
        res.setImageId(vmInfo.getImageId());
        res.setInstanceType(vmInfo.getInstanceType().getId());
        res.setInstanceName(req.getComponent().getComponent().getCnName());
        res.setSystemDiskSize(vmInfo.getSystemDiskSize());
        res.setSystemDiskCategory(vmInfo.getSystemDiskCategory());
        res.setDataDisks(vmInfo.getDataDisks());
        res.setZxAuth(vmInfo.getZxAuth());
        res.setZoneId(vmInfo.getZoneId());
        res.setCreateHost(clusterInfo.getHostName());
        res.setCurrentUser(vmInfo.getCurrentUser());

        res.setNetcardInfos(vmInfo.getNetcardInfos());
        res.setComponentId(req.getComponent().getComponentId());

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
        return CreateSecurityClusterVmInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterVmInitFlowCommandRes.class;
    }
}
