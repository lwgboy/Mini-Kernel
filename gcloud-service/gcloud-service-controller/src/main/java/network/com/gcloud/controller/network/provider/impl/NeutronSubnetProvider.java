package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.CreateSubnetParams;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.provider.ISubnetProvider;
import com.gcloud.controller.network.provider.enums.neutron.NeutronDeviceOwner;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.controller.network.service.ISubnetService;
import com.gcloud.controller.network.service.IVpcService;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NeutronSubnetProvider implements ISubnetProvider {

    @Autowired
    private NeutronProviderProxy proxy;

    @Autowired
    private SubnetDao subnetDao;

    @Autowired
    private NetworkDao networkDao;

    @Autowired
    private RouterDao routerDao;

    @Autowired
    private ISubnetService subnetService;

    @Autowired
    private INetworkService networkService;

    @Autowired
    private IRouterService routerService;

    @Autowired
    private IVpcService vpcService;

    @Autowired
    private IPortService portService;

    @Override
    public ResourceType resourceType() {
        return ResourceType.SUBNET;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public void createSubnet(Network network, String subnetId, CreateSubnetParams params, CurrentUser currentUser) {
        SimpleFlowChain<org.openstack4j.model.network.Subnet, String> chain = new SimpleFlowChain<>("createSubnet");
        chain.then(new Flow<org.openstack4j.model.network.Subnet>("createSubnet") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Subnet data) {
                org.openstack4j.model.network.Subnet net = proxy.createSubnet(network.getProviderRefId(), params.getSubnetName(), params.getCidrBlock());
                chain.data(net);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.Subnet data) {
                proxy.deleteSubnet(data.getId());
            }

        }).then(new NoRollbackFlow<org.openstack4j.model.network.Subnet>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Subnet data) {
                Subnet subnet = new Subnet();
                subnet.setId(subnetId);
                subnet.setCidr(params.getCidrBlock());
                subnet.setName(params.getSubnetName());
                subnet.setUserId(currentUser.getId());
                subnet.setCreateTime(new Date());
                subnet.setNetworkId(network.getId());
                subnet.setZoneId(params.getZoneId());
                subnet.setProvider(providerType().getValue());
                subnet.setProviderRefId(data.getId());
                subnet.setTenantId(currentUser.getDefaultTenant());
                subnetDao.save(subnet);
                chain.next();
            }
        }).then(new NoRollbackFlow<org.openstack4j.model.network.Subnet>() {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Subnet data) {
                PortListOptions options = PortListOptions.create().deviceOwner(NeutronDeviceOwner.DHCP.getNeutronDeviceOwner()).networkId(data.getNetworkId());

                List<? extends Port> ports = null;

                //2分钟超时
                long timeout = 120000L;
                long start = System.currentTimeMillis();
                boolean succ = false;
                while(System.currentTimeMillis() - start <= timeout){
                    ports = proxy.listPort(options);
                    if(ports != null && ports.size() > 0){
                        IPortProvider portProvider = ResourceProviders.get(ResourceType.PORT, ProviderType.NEUTRON.getValue());
                        for(Port port : ports){
                            for(IP fixIp : port.getFixedIps()){
                                if(data.getId().equals(fixIp.getSubnetId())){
                                    //数据库又事务回滚，需要手动回滚是，请拆开多个 Flow
                                    portProvider.createPortData(port.getId(), null, currentUser);
                                    break;
                                }
                            }

                        }
                        succ = true;
                        break;
                    }
                }

                //超时没有成功
                if(!succ){
                    throw new GCloudException("::创建dhcp port 失败");
                }


                chain.next();

            }

        }).done(new FlowDoneHandler<org.openstack4j.model.network.Subnet>() {
            @Override
            public void handle(org.openstack4j.model.network.Subnet data) {
                chain.setResult(data.getId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }


    }

    @Override
    public void deleteSubnet(String subnetRefId) {
        proxy.deleteSubnet(subnetRefId);
    }

    @Override
    public void modifyAttribute(String subnetRefId, String subnetName) {
        proxy.modifySubnetAttribute(subnetRefId, subnetName);
    }

    @Override
    public List<Subnet> list(Map<String, String> filter) {
        List<org.openstack4j.model.network.Subnet> subnets = proxy.listSubnet(filter);
        List<Subnet> retList = new ArrayList<>();
        for (org.openstack4j.model.network.Subnet s : subnets) {
            Subnet sn = new Subnet();
            sn.setCidr(s.getCidr());
            sn.setId(s.getId());
            sn.setName(s.getName());
            sn.setNetworkId(s.getNetworkId());
//            sn.setRouterId();
            sn.setUpdatedAt(s.getUpdatedAt());
            // TODO: other items.

            retList.add(sn);
        }

        return retList;
    }

}
