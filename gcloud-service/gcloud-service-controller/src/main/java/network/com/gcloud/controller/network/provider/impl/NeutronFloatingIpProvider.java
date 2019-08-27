package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.controller.network.provider.IFloatingIpProvider;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class NeutronFloatingIpProvider implements IFloatingIpProvider {

    @Autowired
    private NeutronProviderProxy proxy;

    @Autowired
    private FloatingIpDao floatingipDao;
    
    @Autowired
    private PortDao portDao;
    
    @Autowired
    private NetworkDao networkDao;
    
    @Autowired
    private SubnetDao subnetDao;
    
    @Autowired
    private NeutronPortProvider neutronPortProvider;

    @Override
    public ResourceType resourceType() {
        return ResourceType.FLOATING_IP;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public AllocateEipAddressResponse allocateEipAddress(String networkId, String regionId, CurrentUser currentUser) {

        String floatingIpId = StringUtils.genUuid();
        String portId = StringUtils.genUuid();
        
        
        Network network = networkDao.getById(networkId);
        if(network == null) {
        	throw new GCloudException("0050102::该网络不存在");
        }
        List<Subnet> subents = subnetDao.findByProperty("network_id", networkId);
        if(subents.size() < 1) {
        	throw new GCloudException("0050103::该网络不包含任何子网");
        }
        
        SimpleFlowChain<com.gcloud.controller.network.model.AllocateEipAddressResponse, com.gcloud.controller.network.model.AllocateEipAddressResponse> chain = new SimpleFlowChain<>(
                "allocateEipAddress");
        chain.then(new Flow<com.gcloud.controller.network.model.AllocateEipAddressResponse>("allocateEipAddress") {
        	
        	String portId = null;
        	
        	@Override
            public void run(SimpleFlowChain chain, com.gcloud.controller.network.model.AllocateEipAddressResponse data) {
                AllocateEipAddressResponse res = proxy.allocateEipAddress(network.getProviderRefId());
                res.setAllocationRefId(res.getAllocationId());
                res.setAllocationId(floatingIpId);                 
                   
                chain.data(res);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, com.gcloud.controller.network.model.AllocateEipAddressResponse data) {
                proxy.releaseEipAddress(data.getAllocationRefId());
            }
              
        }).then(new NoRollbackFlow<com.gcloud.controller.network.model.AllocateEipAddressResponse>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, com.gcloud.controller.network.model.AllocateEipAddressResponse data) {
                if (data.getAllocationId() != null) {

                    FloatingIp floatingip = new FloatingIp();
                    floatingip.setId(floatingIpId);
                    floatingip.setRegionId(regionId);
                    floatingip.setFloatingNetworkId(networkId);
                    floatingip.setUserId(currentUser.getId());
                    floatingip.setTenantId(currentUser.getDefaultTenant());
                    floatingip.setCreateTime(new Date());
                    floatingip.setFloatingIpAddress(data.getEipAddress());
//                    floatingip.setFloatingPortId(data.getPortId());
                    floatingip.setRouterId(data.getRouterId());
                    floatingip.setStatus(ResourceStates.status(ResourceType.FLOATING_IP, ProviderType.NEUTRON, data.getStatus()));
                    floatingip.setProvider(providerType().getValue());
                    floatingip.setProviderRefId(data.getAllocationId());      
                    floatingip.setFloatingPortId(portId);
                    
                    floatingipDao.save(floatingip);

                    //floatingIp对应的port

                    PortListOptions options = PortListOptions.create().deviceId(data.getAllocationRefId());
                    List<? extends org.openstack4j.model.network.Port> ports = proxy.listPort(options);
                    if(ports != null && ports.size() > 0){
                        org.openstack4j.model.network.Port neutronPort = proxy.getPort(ports.get(0).getId());
                        neutronPortProvider.createPortData(neutronPort.getId(), floatingIpId, currentUser);
                    }
                }
                chain.next();
            }
        }).done(new FlowDoneHandler<com.gcloud.controller.network.model.AllocateEipAddressResponse>() {
            @Override
            public void handle(com.gcloud.controller.network.model.AllocateEipAddressResponse data) {
                chain.setResult(data);
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }

        /*AllocateEipAddressResponse res = proxy.allocateEipAddress(networkId);
        if (res.getAllocationId() != null) {
        
            FloatingIp floatingip = new FloatingIp();
            floatingip.setId(res.getAllocationId());
            floatingip.setRegionId(regionId);
            floatingip.setFloatingNetworkId(networkId);
            floatingip.setUserId(userId);
            floatingip.setCreateTime(new Date());
            floatingip.setFloatingIpAddress(res.getEipAddress());
            floatingip.setFloatingPortId(res.getPortId());
            floatingip.setRouterId(res.getRouterId());
            floatingip.setStatus(res.getStatus());
            floatingipDao.save(floatingip);
        }*/

        return chain.getResult();
    }

    @Override
    public void associateEipAddress(String allocationRefId, String netcardId) {
        proxy.associateEipAddress(allocationRefId, netcardId);
    }

    @Override
    public void unAssociateEipAddress(String allocationRefId) {
        proxy.unAssociateEipAddress(allocationRefId);
    }

    @Override
    public void releaseEipAddress(String allocationRefId) {
        proxy.releaseEipAddress(allocationRefId);
    }

    @Override
    public List<FloatingIp> list(Map<String, String> filter) {
        List<org.openstack4j.model.network.NetFloatingIP> fips = proxy.listFloatingIps(filter);
        List<FloatingIp> retList = new ArrayList<>();
        for (org.openstack4j.model.network.NetFloatingIP ip : fips) {
            FloatingIp fi = new FloatingIp();
            fi.setUpdatedAt(ip.getUpdatedAt());
            fi.setStatus(ResourceStates.status(ResourceType.FLOATING_IP, ProviderType.NEUTRON, ip.getStatus()));
            fi.setFixedPortId(ip.getPortId());
//            fi.setId(ip.getId());
            fi.setFloatingIpAddress(ip.getFloatingIpAddress());
            fi.setFloatingPortId(ip.getPortId());
            fi.setRouterId(ip.getRouterId());
            fi.setFloatingNetworkId(ip.getFloatingNetworkId());
            fi.setProvider(providerType().getValue());
            fi.setProviderRefId(ip.getId());
            // TODO: still some items not setup in fi.

            retList.add(fi);
        }

        return retList;
    }
}
