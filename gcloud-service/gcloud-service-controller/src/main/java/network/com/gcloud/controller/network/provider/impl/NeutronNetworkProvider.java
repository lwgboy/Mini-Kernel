package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.model.CreateNetworkParams;
import com.gcloud.controller.network.provider.INetworkProvider;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.msg.api.CreateExternalNetworkMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class NeutronNetworkProvider implements INetworkProvider {

    @Autowired
    private NeutronProviderProxy proxy;

    @Autowired
    private NetworkDao networkDao;

    @Override
    public ResourceType resourceType() {
        return ResourceType.NETWORK;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public void createNetwork(String networkId, CreateNetworkParams params, CurrentUser currentUser) {
        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("createNetwork");
        chain.then(new Flow<org.openstack4j.model.network.Network>("createNetwork") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Network data) {
            	org.openstack4j.model.network.Network network = proxy.createNetwork(params.getNetworkName());
                chain.data(network);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.Network data) {
                proxy.removeExternalNetwork(data.getId());
            }

        }).then(new NoRollbackFlow<org.openstack4j.model.network.Network>("save to db") {
            @Override
            public void run(SimpleFlowChain chain,org.openstack4j.model.network.Network data) {
                Network network = new Network();
                network.setId(networkId);
                network.setName(params.getNetworkName());
                network.setStatus(ResourceStates.status(ResourceType.NETWORK, ProviderType.NEUTRON, data.getStatus().name()));
                network.setRegionId(params.getRegionId());
                network.setProvider(providerType().getValue());
                network.setProviderRefId(data.getId());
                network.setTenantId(currentUser.getDefaultTenant());
                network.setCreateTime(new Date());
                network.setType(0);
                networkDao.save(network);

                chain.next();
            }
        }).done(new FlowDoneHandler<org.openstack4j.model.network.Network>() {
            @Override
            public void handle(org.openstack4j.model.network.Network data) {
                chain.setResult(data.getId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }
    }

    @Override
    public void removeNetwork(String vpcRefId) {
        proxy.removeExternalNetwork(vpcRefId);
    }

    @Override
    public void updateNetwork(String vpcRefId, String vpcName) {
        org.openstack4j.model.network.Network nw = proxy.getExternalNetwork(vpcRefId);
        if (nw != null) {
            nw.setName(vpcName);
            proxy.updateExternalNetwork(vpcRefId, vpcName);
        }
    }

    @Override
    public List<Network> list(Map<String, String> filter) {
        List<org.openstack4j.model.network.Network> networks = proxy.listNetwork(filter);
        List<Network> nwList = new ArrayList<>();
        for (org.openstack4j.model.network.Network n : networks) {
            Network net = new Network();
            net.setName(n.getName());
            net.setStatus(ResourceStates.status(ResourceType.NETWORK, ProviderType.NEUTRON, n.getStatus().name()));
            net.setProvider(providerType().getValue());
            net.setProviderRefId(n.getId());
            net.setUpdatedAt(n.getUpdatedAt());
            // and region...

            nwList.add(net);
        }

        return nwList;
    }

	@Override
	public void createExternalNetwork(String networkId, CreateExternalNetworkMsg msg) {
		// TODO Auto-generated method stub
		SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("createNetwork");
        chain.then(new Flow<org.openstack4j.model.network.Network>("createNetwork") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Network data) {
            	org.openstack4j.model.network.Network network = proxy.createExternalNetwork(msg.getNetworkName());
                chain.data(network);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.Network data) {
                proxy.removeExternalNetwork(data.getId());
            }

        }).then(new NoRollbackFlow<org.openstack4j.model.network.Network>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Network data) {
                Network network = new Network();
                network.setId(networkId);
                network.setName(msg.getNetworkName());
                network.setStatus(ResourceStates.status(ResourceType.NETWORK, ProviderType.NEUTRON, data.getStatus().name()));
                network.setRegionId(msg.getRegion());
                network.setProvider(providerType().getValue());
                network.setProviderRefId(data.getId());
                network.setTenantId(msg.getCurrentUser().getDefaultTenant());
                network.setCreateTime(new Date());
                network.setType(1);
                networkDao.save(network);

                chain.next();
            }
        }).done(new FlowDoneHandler<org.openstack4j.model.network.Network>() {
            @Override
            public void handle(org.openstack4j.model.network.Network data) {
                chain.setResult(data.getId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }
	}
}
