package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.dao.SecurityClusterDao;
import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.enums.SecurityClusterState;
import com.gcloud.controller.security.model.CreateClusterParams;
import com.gcloud.controller.security.model.CreateClusterResponse;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.CreateSecurityClusterInitFlowCommandRes;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class CreateSecurityClusterInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private ISecurityClusterService securityClusterService;

    @Autowired
    private SecurityClusterComponentDao securityClusterComponentDao;

    @Autowired
    private SecurityClusterOvsBridgeDao securityClusterOvsBridgeDao;

    @Autowired
    private SecurityClusterDao securityClusterDao;

    @Override
    protected Object process() throws Exception {

        CreateSecurityClusterInitFlowCommandReq request = (CreateSecurityClusterInitFlowCommandReq)getReqParams();
        CreateSecurityClusterInitFlowCommandRes response = new CreateSecurityClusterInitFlowCommandRes();

        CreateClusterParams params = BeanUtil.copyBean(request, CreateClusterParams.class);
        CreateClusterResponse createInfo = securityClusterService.createCluster(params, request.getCurrentUser());

        response.setBridgeInfos(createInfo.getBridgeInfos());
        response.setClusterId(createInfo.getId());
        response.setComponents(createInfo.getComponents());
        response.setHa(createInfo.getHa());
        response.setClusterId(createInfo.getId());

        return response;
    }

    @Override
    protected Object rollback() throws Exception {
        //TODO
        //清空所有数据库,
        CreateSecurityClusterInitFlowCommandRes res = (CreateSecurityClusterInitFlowCommandRes)getResParams();

        List<SecurityClusterComponent> components = securityClusterComponentDao.findByProperty(SecurityClusterComponent.CLUSTER_ID, res.getClusterId());
        List<SecurityClusterOvsBridge> ovsBridges = securityClusterOvsBridgeDao.findByProperty(SecurityClusterOvsBridge.CLUSTER_ID, res.getClusterId());

        //都回滚清理完成
        if((components == null || components.size() == 0) && (ovsBridges == null || ovsBridges.size() == 0)){
            securityClusterService.cleanClusterData(res.getClusterId());
        }else{
            securityClusterDao.changeState(res.getClusterId(), SecurityClusterState.FAILED.value());
        }

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CreateSecurityClusterInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CreateSecurityClusterInitFlowCommandRes.class;
    }
}
