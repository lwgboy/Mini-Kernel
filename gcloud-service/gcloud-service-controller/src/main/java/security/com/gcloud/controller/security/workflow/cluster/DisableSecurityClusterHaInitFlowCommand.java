package com.gcloud.controller.security.workflow.cluster;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.model.PortInfo;
import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.dao.SecurityClusterSubnetDao;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.entity.SecurityClusterSubnet;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.enums.SecurityNetworkType;
import com.gcloud.controller.security.model.workflow.DisableSecurityClusterHaInitFlowCommandReq;
import com.gcloud.controller.security.model.workflow.DisableSecurityClusterHaInitFlowCommandRes;
import com.gcloud.controller.security.model.workflow.SecurityClusterComponentHaNetcardInfo;
import com.gcloud.controller.security.model.workflow.SecurityClusterComponentInfo;
import com.gcloud.controller.security.model.workflow.SecurityClusterOvsBridgeInfo;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Slf4j
public class DisableSecurityClusterHaInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private SecurityClusterComponentDao clusterComponentDao;

    @Autowired
    private SecurityClusterOvsBridgeDao clusterOvsBridgeDao;

    @Autowired
    private PortDao portDao;

    @Autowired
    private SecurityClusterSubnetDao securityClusterSubnetDao;

    @Autowired
    private ISecurityClusterService securityClusterService;

    @Override
    protected Object process() throws Exception {

        DisableSecurityClusterHaInitFlowCommandReq req = (DisableSecurityClusterHaInitFlowCommandReq)getReqParams();

        securityClusterService.disable(req.getClusterId(), req.getCurrentUser());

        Map<String, Object> haComponentFilter = new HashMap<>();
        haComponentFilter.put(SecurityClusterComponent.CLUSTER_ID, req.getClusterId());
        haComponentFilter.put(SecurityClusterComponent.HA, true);
        List<SecurityClusterComponentInfo> components = clusterComponentDao.findByProperties(haComponentFilter, SecurityClusterComponentInfo.class);

        Map<String, Object> haOvsBridgeFilter = new HashMap<>();
        haOvsBridgeFilter.put(SecurityClusterOvsBridge.CLUSTER_ID, req.getClusterId());
        haOvsBridgeFilter.put(SecurityClusterOvsBridge.HA, true);
        List<SecurityClusterOvsBridgeInfo> ovsBridges = clusterOvsBridgeDao.findByProperties(haOvsBridgeFilter, SecurityClusterOvsBridgeInfo.class);

        //防火墙
        Map<String, Object> firewallComFilter = new HashMap<>();
        firewallComFilter.put(SecurityClusterComponent.CLUSTER_ID, req.getClusterId());
        firewallComFilter.put(SecurityClusterComponent.TYPE, SecurityComponent.FIREWALL.value());
        firewallComFilter.put(SecurityClusterComponent.HA, false);
        SecurityClusterComponent firewall = clusterComponentDao.findUniqueByProperties(firewallComFilter);

        List<SecurityClusterComponentHaNetcardInfo> haNetcardInfos = new ArrayList<>();

        if(firewall != null){
            //ha网络
            Map<String, Object> haSubnetFilter = new HashMap<>();
            haSubnetFilter.put(SecurityClusterSubnet.CLUSTER_ID, req.getClusterId());
            haSubnetFilter.put(SecurityClusterSubnet.NETWORK_TYPE, SecurityNetworkType.HA.value());
            SecurityClusterSubnet haSubnet = securityClusterSubnetDao.findUniqueByProperties(haSubnetFilter);

            if(haSubnet != null){
                List<PortInfo> portInfos = portDao.getInstancePortAndIp(firewall.getObjectId(), PortInfo.class);

                String firewallHaNetcardId = null;
                for(PortInfo portInfo : portInfos){
                    if(StringUtils.isNotBlank(portInfo.getSubnetId()) && portInfo.getSubnetId().equals(haSubnet.getSubnetId())){
                        firewallHaNetcardId = portInfo.getId();
                    }
                }

                if(StringUtils.isNotBlank(firewallHaNetcardId)){
                    SecurityClusterComponentHaNetcardInfo firewallNetcardInfo = new SecurityClusterComponentHaNetcardInfo();
                    firewallNetcardInfo.setComponentId(firewall.getId());
                    firewallNetcardInfo.setObjectId(firewall.getObjectId());
                    firewallNetcardInfo.setObjectType(firewall.getObjectType());
                    firewallNetcardInfo.setNetcardId(firewallHaNetcardId);
                    haNetcardInfos.add(firewallNetcardInfo);
                }
            }

        }


        DisableSecurityClusterHaInitFlowCommandRes res = new DisableSecurityClusterHaInitFlowCommandRes();
        res.setClusterId(req.getClusterId());
        res.setComponents(components);
        res.setOvsBridges(ovsBridges);
        res.setHaNetcardInfos(haNetcardInfos);

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
        return DisableSecurityClusterHaInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DisableSecurityClusterHaInitFlowCommandRes.class;
    }
}
