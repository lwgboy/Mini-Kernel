package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.dao.RouterPortDao;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.entity.RouterPort;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.provider.IRouterProvider;
import com.gcloud.controller.network.provider.enums.neutron.NeutronDeviceOwner;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class NeutronRouterProvider implements IRouterProvider {

    @Autowired
    private NeutronProviderProxy proxy;

    @Autowired
    private RouterDao vRouterDao;

    @Autowired
    private RouterPortDao routerPortDao;


    @Override
    public ResourceType resourceType() {
        return ResourceType.ROUTER;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public String createVRouter(CreateVRouterMsg msg) {

        String routerId = StringUtils.genUuid();
        
        SimpleFlowChain<org.openstack4j.model.network.Router, String> chain = new SimpleFlowChain<>("create router");
        chain.then(new Flow<org.openstack4j.model.network.Router>("create router") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Router data) {
                org.openstack4j.model.network.Router router = proxy.createRouter(msg.getvRouterName());
                chain.data(router);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.Router data) {
                proxy.deleteRouter(data.getId());
            }

        }).then(new NoRollbackFlow<org.openstack4j.model.network.Router>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Router data) {
                Router vRouter = new Router();
                vRouter.setId(routerId);
                vRouter.setName(msg.getvRouterName());
                vRouter.setUserId(msg.getCurrentUser().getId());
                vRouter.setTenantId(msg.getCurrentUser().getDefaultTenant());
                vRouter.setCreateTime(new Date());
                vRouter.setStatus(ResourceStates.status(ResourceType.ROUTER, ProviderType.NEUTRON, data.getStatus().name()));
                vRouter.setProvider(providerType().getValue());
                vRouter.setProviderRefId(data.getId());
                vRouter.setRegionId(msg.getRegion());
                vRouterDao.save(vRouter);
                chain.next();
            }
        }).done(new FlowDoneHandler<org.openstack4j.model.network.Router>() {
            @Override
            public void handle(org.openstack4j.model.network.Router data) {
                chain.setResult(data.getId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }

        return routerId;

    }

    @Override
    public void deleteVRouter(String routerRefId) {
        proxy.deleteRouter(routerRefId);
    }

    @Override
    public void modifyVRouterAttribute(String routerRefId, String vRouterName) {
        proxy.updateRouter(routerRefId, vRouterName);
    }

    @Override
    public void setVRouterGateway(String routerId, String routerRefId, String networkId, String networkRefId, CurrentUser currentUser) {

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>();
        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                proxy.setVRouterGateway(routerRefId, networkRefId);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                proxy.cleanVRouterGateway(routerRefId);
            }

        }).then(new Flow<String>(){
            @Override
            public void run(SimpleFlowChain chain, String data) {

                PortListOptions options = PortListOptions.create().deviceId(routerRefId).deviceOwner(NeutronDeviceOwner.GATEWAY.getNeutronDeviceOwner());
                List<? extends Port> ports = proxy.listPort(options);
                if(ports != null && ports.size() > 0){
                    IPortProvider portProvider = ResourceProviders.get(ResourceType.PORT, ProviderType.NEUTRON.getValue());
                    for(Port port : ports){
                        com.gcloud.controller.network.entity.Port gcPort = portProvider.createPortData(port.getId(), networkId, currentUser);
                        RouterPort routerPort = new RouterPort();
                        routerPort.setRouterId(routerId);
                        routerPort.setPortId(gcPort.getId());
                        routerPort.setPortType(DeviceOwner.GATEWAY.getValue());
                        routerPortDao.save(routerPort);
                    }
                }
                chain.next();

            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

            }

        }).start();



    }

    @Override
    public void cleanVRouterGateway(String routerRefId) {
        proxy.cleanVRouterGateway(routerRefId);
    }

    @Override
    public String attachSubnetRouter(String routerRefId, String subnetId) {
        RouterInterface routerInterface = proxy.attachSubnetRouter(routerRefId, subnetId);
        return routerInterface.getPortId();
    }

    @Override
    public void detachSubnetRouter(String routerRefId, String subnetId) {
    	detachSubnetRouter(routerRefId, subnetId, null);
    }
    
    @Override
    public void detachSubnetRouter(String routerRefId, String subnetId, String portId) {
        proxy.detachSubnetRouter(routerRefId, subnetId, portId);
    }

    @Override
    public List<Router> list(Map<String, String> filter) {
        // neutron route list interface does not support filter.
        List<org.openstack4j.model.network.Router> sRouters = proxy.listRouter();
        List<Router> routers = new ArrayList<>();
        for (org.openstack4j.model.network.Router r : sRouters) {
            Router router = new Router();
            router.setProvider(providerType().getValue());
            router.setProviderRefId(r.getId());
            router.setName(r.getName());
            router.setStatus(ResourceStates.status(ResourceType.ROUTER, ProviderType.NEUTRON, r.getStatus().name()));
            router.setUpdatedAt(r.getUpdatedAt());

            routers.add(router);
        }

        return routers;
    }

	@Override
	public org.openstack4j.model.network.Router getVRouter(String routerRefId) {
		// TODO Auto-generated method stub
		return proxy.getRouter(routerRefId);
	}

}
