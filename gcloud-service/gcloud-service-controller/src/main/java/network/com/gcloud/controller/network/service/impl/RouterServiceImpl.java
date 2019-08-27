package com.gcloud.controller.network.service.impl;

import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.dao.RouterPortDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.entity.RouterPort;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.DescribeVRoutersParams;
import com.gcloud.controller.network.model.RouterAttachInfo;
import com.gcloud.controller.network.model.RouterSubnetInfo;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.provider.IRouterProvider;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.VRouterSetType;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;
import com.gcloud.header.network.msg.api.ModifyVRouterAttributeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RouterServiceImpl implements IRouterService {
    @Autowired
    private RouterDao vRouterDao;

    @Autowired
    private SubnetDao subnetDao;
    
    @Autowired
    private NetworkDao networkDao;

    @Autowired
    private IPortService portService;

    @Autowired
    private RouterPortDao routerPortDao;

    @Autowired
    private PortDao portDao;

    @Override
    public PageResult<VRouterSetType> describeVRouters(DescribeVRoutersParams params, CurrentUser currentUser) {
        if (params == null) {
            params = new DescribeVRoutersParams();
        }
        return vRouterDao.describeVRouters(params, VRouterSetType.class, currentUser);
    }

    @Override
    public String createVRouter(CreateVRouterMsg msg) {
        return this.getProviderOrDefault().createVRouter(msg);
    }

    @Override
    public Router getVRouterById(String vRouterId) {
        return vRouterDao.getById(vRouterId);
    }

    @Override
    public void deleteVRouter(String routerId) {
        Router router = vRouterDao.getById(routerId);
        if (null == router) {
            throw new GCloudException("0020402::找不到该路由器");
        }

        if(routerPortDao.hasPort(routerId, DeviceOwner.ROUTER.getValue())){
            throw new GCloudException("0020406::有关联的子网，不能删除");
        }

        List<RouterPort> routerPorts = routerPortDao.findByProperty(RouterPort.ROUTER_ID, routerId);
        if(routerPorts != null && routerPorts.size() > 0){
            for(RouterPort routerPort : routerPorts){
                portService.cleanPortData(routerPort.getPortId());
            }
        }

        routerPortDao.deleteByRouter(routerId);
        vRouterDao.deleteById(routerId);
        this.checkAndGetProvider(router.getProvider()).deleteVRouter(router.getProviderRefId());
    }

    @Override
    public void modifyVRouterAttribute(ModifyVRouterAttributeMsg msg) {
        Router router = vRouterDao.getById(msg.getvRouterId());
        if (router == null) {
            throw new GCloudException("0020203::修改失败，找不到该路由器");
        }
        List<String> updatedField = new ArrayList<String>();
        updatedField.add(router.updateName(msg.getvRouterName()));
        updatedField.add(router.updateUpdatedAt(new Date()));
        vRouterDao.update(router);
        CacheContainer.getInstance().put(CacheType.ROUTER_NAME, msg.getvRouterId(), msg.getvRouterName());
        this.checkAndGetProvider(router.getProvider()).modifyVRouterAttribute(router.getProviderRefId(), msg.getvRouterName());
    }

    @Override
    public void setVRouterGateway(String vRouterId, String networkId, CurrentUser currentUser) {
        Router router = vRouterDao.getById(vRouterId);
        if (router == null) {
            throw new GCloudException("0020503::找不到该路由器");
        }

        if(routerPortDao.hasPort(vRouterId, DeviceOwner.GATEWAY.getValue())){
            throw new GCloudException("0020505::已经关联网关，请先清除网关");
        }

        Network network = networkDao.getById(networkId);
        if (network == null) {
            throw new GCloudException("0020504::找不到该网络");
        }

        List<String> updatedField = new ArrayList<>();
        updatedField.add(router.updateExternalGatewayNetworkId(networkId));
        updatedField.add(router.updateUpdatedAt(new Date()));
        vRouterDao.update(router, updatedField);

        this.checkAndGetProvider(router.getProvider()).setVRouterGateway(router.getId(), router.getProviderRefId(), network.getId(), network.getProviderRefId(), currentUser);

    }

    @Override
    public void cleanVRouterGateway(String vRouterId) {
        Router router = vRouterDao.getById(vRouterId);
        if (router == null) {
            throw new GCloudException("0020602::找不到该路由器");
        }

        Map<String, Object> routerPortFilter = new HashMap<>();
        routerPortFilter.put(RouterPort.ROUTER_ID, vRouterId);
        routerPortFilter.put(RouterPort.PORT_TYPE, DeviceOwner.GATEWAY.getValue());
        List<RouterPort> routerPorts = routerPortDao.findByProperties(routerPortFilter);
        if(routerPorts == null || routerPorts.size() == 0){
            throw new GCloudException("::没有关联网关");
        }


        for(RouterPort routerPort : routerPorts){
            routerPortDao.delete(routerPort);
            portService.cleanPortData(routerPort.getPortId());
        }

        List<String> updatedField = new ArrayList<>();
        updatedField.add(router.updateExternalGatewayNetworkId(null));
        updatedField.add(router.updateUpdatedAt(new Date()));
        vRouterDao.update(router, updatedField);

        this.checkAndGetProvider(router.getProvider()).cleanVRouterGateway(router.getProviderRefId());
    }

    @Override
    public void attachVSwitchVRouter(String routerId, String subnetId, CurrentUser currentUser) {
        Router router = vRouterDao.getById(routerId);
        if (router == null) {
            throw new GCloudException("0020703::找不到该路由器");
        }
        Subnet subnet = subnetDao.getById(subnetId);
        if (subnet == null) {
            throw new GCloudException("0020704::找不到该交换机");
        }

        IRouterProvider provider = this.checkAndGetProvider(router.getProvider());
        IPortProvider portProvider = ResourceProviders.checkAndGet(ResourceType.PORT, ProviderType.NEUTRON.getValue());

        RouterAttachInfo attachInfo = new RouterAttachInfo();
        SimpleFlowChain<RouterAttachInfo, String> chain = new SimpleFlowChain<>("attach router subnet");
        chain.data(attachInfo);
        chain.then(new Flow<RouterAttachInfo>() {
            @Override
            public void run(SimpleFlowChain chain, RouterAttachInfo data) {
                String refPortId = provider.attachSubnetRouter(router.getProviderRefId(), subnet.getProviderRefId());
                data.setRefPortId(refPortId);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, RouterAttachInfo data) {
                provider.detachSubnetRouter(router.getProviderRefId(), subnet.getProviderRefId(), data.getRefPortId());
            }
        }).then(new Flow<RouterAttachInfo>() {
            @Override
            public void run(SimpleFlowChain chain, RouterAttachInfo data) {
                Port port = portProvider.createPortData(data.getRefPortId(), routerId, currentUser);
                data.setPort(port);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, RouterAttachInfo data) {
                portService.cleanPortData(data.getPort().getId());
            }
        }).then(new NoRollbackFlow<RouterAttachInfo>() {
            @Override
            public void run(SimpleFlowChain chain, RouterAttachInfo data) {

                RouterPort routerPort = new RouterPort();
                routerPort.setPortId(data.getPort().getId());
                routerPort.setPortType(data.getPort().getDeviceOwner());
                routerPort.setRouterId(routerId);
                routerPortDao.save(routerPort);
                chain.next();
            }

        }).start();

    }

    @Override
    public void detachVSwitchVRouter(String routerId, String subnetId) {
    	detachVSwitchVRouter(routerId, subnetId, null);
    }
    
    @Override
    public void detachVSwitchVRouter(String routerId, String subnetId, String portId) {
        Router router = vRouterDao.getById(routerId);
        if (router == null) {
            throw new GCloudException("0020803::找不到该路由器");
        }
        Subnet subnet = subnetDao.getById(subnetId);
        if (subnet == null) {
            throw new GCloudException("0020804::找不到该交换机");
        }
        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("::找不到端口");
        }

        List<RouterSubnetInfo> routerAttachInfos = routerPortDao.routerSubnetInfos(routerId, subnetId);
        if(routerAttachInfos != null && routerAttachInfos.size() > 0){
            for(RouterSubnetInfo info : routerAttachInfos){
                routerPortDao.delete(info.getRouterId(), info.getPortId());
                portService.cleanPortData(info.getPortId());
            }
        }


        this.checkAndGetProvider(router.getProvider()).detachSubnetRouter(router.getProviderRefId(), subnet.getProviderRefId(), port.getProviderRefId());
    }

    private IRouterProvider getProviderOrDefault() {
        IRouterProvider provider = ResourceProviders.getDefault(ResourceType.ROUTER);
        return provider;
    }

    private IRouterProvider checkAndGetProvider(Integer providerType) {
        IRouterProvider provider = ResourceProviders.checkAndGet(ResourceType.ROUTER, providerType);
        return provider;
    }

	@Override
	public int routerStatistics() {
		return vRouterDao.routerStatistics();
	}

}
