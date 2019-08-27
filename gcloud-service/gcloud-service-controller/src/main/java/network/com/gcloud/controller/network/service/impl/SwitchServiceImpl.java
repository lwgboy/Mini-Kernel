package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.RouterPortDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.RouterPort;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.CreateNetworkParams;
import com.gcloud.controller.network.model.CreateSubnetParams;
import com.gcloud.controller.network.model.DescribeVSwitchesParams;
import com.gcloud.controller.network.model.VSwitchProxyData;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.controller.network.service.ISubnetService;
import com.gcloud.controller.network.service.ISwitchService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.network.model.VSwitchSetType;
import com.gcloud.header.network.msg.api.CreateVSwitchMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchServiceImpl implements ISwitchService {

    @Autowired
    private SubnetDao subnetDao;

    @Autowired
    private INetworkService networkService;

    @Autowired
    private ISubnetService subnetService;

    @Autowired
    private IRouterService routerService;

    @Autowired
    private PortDao portDao;

    @Autowired
    private RouterPortDao routerPortDao;

    @Override
    public PageResult<VSwitchSetType> describeVSwitches(DescribeVSwitchesParams params, CurrentUser currentUser) {
        return subnetDao.describeVSwitches(params, VSwitchSetType.class, currentUser);
    }

    @Override
    public String createVSwitch(CreateVSwitchMsg params, CurrentUser currentUser) {

        VSwitchProxyData vswitch = new VSwitchProxyData();

        SimpleFlowChain<VSwitchProxyData, String> chain = new SimpleFlowChain<>("createVSwitch");
        chain.data(vswitch);

        chain.then(new Flow<VSwitchProxyData>("createVSwitch") {
            @Override
            public void run(SimpleFlowChain chain, VSwitchProxyData data) {
                //已经有网络
                if(StringUtils.isBlank(params.getNetworkId())){
                    CreateNetworkParams netParam = new CreateNetworkParams();
                    netParam.setNetworkName(String.format("vpcId:%s-network", params.getVpcId()));
                    netParam.setRegionId(params.getRegionId());
                    String networkId = networkService.createNetwork(netParam, currentUser);
                    data.setNetworkId(networkId);
                }else{
                    data.setNetworkId(params.getNetworkId());
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, VSwitchProxyData data) {

                if(StringUtils.isBlank(params.getNetworkId())){
                    networkService.removeNetwork(data.getNetworkId());
                }

            }

        }).then(new Flow<VSwitchProxyData>("createVSwitch") {
            @Override
            public void run(SimpleFlowChain chain, VSwitchProxyData data) {

                CreateSubnetParams createSubnetParam = new CreateSubnetParams();
                createSubnetParam.setCidrBlock(params.getCidrBlock());
                createSubnetParam.setNetworkId(data.getNetworkId());
                createSubnetParam.setRegionId(params.getRegionId());
                createSubnetParam.setZoneId(params.getZoneId());
                createSubnetParam.setSubnetName(params.getvSwitchName());

                String subnetId = subnetService.createSubnet(createSubnetParam, currentUser);
                data.setSubnetId(subnetId);

                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, VSwitchProxyData data) {

                subnetService.deleteSubnet(data.getSubnetId());
            }

        }).then(new Flow<VSwitchProxyData>("createVSwitch") {
            @Override
            public void run(SimpleFlowChain chain, VSwitchProxyData data) {

                if(StringUtils.isNotBlank(params.getVpcId())){
                    routerService.attachVSwitchVRouter(params.getVpcId(), data.getSubnetId(), currentUser);
                }
                chain.next();

            }

            @Override
            public void rollback(SimpleFlowChain chain, VSwitchProxyData data) {
                if(StringUtils.isNotBlank(params.getVpcId())){
                    routerService.detachVSwitchVRouter(params.getVpcId(), data.getSubnetId());
                }

            }
        }).done(new FlowDoneHandler<VSwitchProxyData>() {
            @Override
            public void handle(VSwitchProxyData data) {
                chain.setResult(data.getSubnetId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }

        return vswitch.getSubnetId();
    }

    @Override
    public void deleteVSwitch(String subnetId) {

        Subnet subnet = subnetDao.getById(subnetId);
        if (null == subnet) {
            throw new GCloudException("0030402::找不到交换机");
        }
        // vpc 情况下 网络下只有一个子网
        List<Port> ports = portDao.findByProperty(Port.NETWORK_ID, subnet.getNetworkId());
        if(ports != null && ports.size() > 0){
            for(Port port : ports){
                if(!DeviceOwner.ROUTER.getValue().equals(port.getDeviceOwner()) && !DeviceOwner.DHCP.getValue().equals(port.getDeviceOwner())){
                    throw new GCloudException("0030404::有关联的端口，不能删除");
                }
            }
        }

        SimpleFlowChain<VSwitchProxyData, String> chain = new SimpleFlowChain<>("deleteVSwitch");

        for(Port port : ports){
            List<RouterPort> routerPorts = routerPortDao.findByProperty(RouterPort.PORT_ID, port.getId());
            if(routerPorts == null || routerPorts.size() == 0){
                continue;
            }

            for(RouterPort routerPort : routerPorts){

                chain.then(new Flow<String>() {
                    @Override
                    public void run(SimpleFlowChain chain, String data) {
                        routerService.detachVSwitchVRouter(routerPort.getRouterId(), subnetId, routerPort.getPortId());
                        chain.next();
                    }

                    @Override
                    public void rollback(SimpleFlowChain chain, String data) {
                        CurrentUser currentUser = new CurrentUser();
                        currentUser.setDefaultTenant(port.getTenantId());
                        currentUser.setId(port.getUserId());
                        routerService.attachVSwitchVRouter(routerPort.getRouterId(), subnetId, currentUser);
                    }
                });
            }
        }

        //没办法回滚，回滚后id不一样
        chain.then(new NoRollbackFlow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                subnetService.deleteSubnet(subnet.getId());
                chain.next();
            }
        }).then(new NoRollbackFlow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                networkService.removeNetwork(subnet.getNetworkId());
                chain.next();
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }

    }

}
