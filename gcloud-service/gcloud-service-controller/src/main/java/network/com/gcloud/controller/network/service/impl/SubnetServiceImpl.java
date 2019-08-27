package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.CreateSubnetParams;
import com.gcloud.controller.network.provider.ISubnetProvider;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.controller.network.service.ISubnetService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.enums.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SubnetServiceImpl implements ISubnetService {
    @Autowired
    private SubnetDao subnetDao;
    @Autowired
    private NetworkDao networkDao;
    @Autowired
    private PortDao portDao;

    @Autowired
    private IPortService portService;

    @Override
    public String createSubnet(CreateSubnetParams params, CurrentUser currentUser) {
        Network network = networkDao.getById(params.getNetworkId());
        if (network == null) {
            throw new GCloudException("0030402::找不到网络");
        }
        String subnetId = StringUtils.genUuid();
        this.getProviderOrDefault().createSubnet(network, subnetId, params, currentUser);
        return subnetId;
    }

    @Override
    public void deleteSubnet(String subnetId) {
        Subnet subnet = subnetDao.getById(subnetId);
        if (null == subnet) {
            throw new GCloudException("0030402::找不到交换机");
        }
        subnetDao.deleteById(subnetId);
        //删除dhcp的poort

        List<Port> ports = portDao.subnetPorts(subnetId);
        if(ports != null && ports.size() > 0){
            for(Port port : ports){
                if(!DeviceOwner.DHCP.getValue().equals(port.getDeviceOwner()) || !DeviceOwner.GATEWAY.getValue().equals(port.getDeviceOwner())){
                    throw new GCloudException("0030403::有关联的端口，不能删除");
                }
            }

            for(Port port : ports){
                portService.cleanPortData(port.getId());
            }
        }

        this.checkAndGetProvider(subnet.getProvider()).deleteSubnet(subnet.getProviderRefId());
        
    }

    @Override
    public void modifyAttribute(String subnetId, String subnetName) {
        Subnet subnet = subnetDao.getById(subnetId);
        if (null == subnet) {
            throw new GCloudException("0030203::找不到交换机");
        }
        List<String> updateField = new ArrayList<String>();
        updateField.add(subnet.updateName(subnetName));
        updateField.add(subnet.updateUpdatedAt(new Date()));
        subnetDao.update(subnet, updateField);
        CacheContainer.getInstance().put(CacheType.SUBNET_NAME, subnetId, subnetName);
        
        this.checkAndGetProvider(subnet.getProvider()).modifyAttribute(subnet.getProviderRefId(), subnetName);
        
    }

    private ISubnetProvider getProviderOrDefault() {
        ISubnetProvider provider = ResourceProviders.getDefault(ResourceType.SUBNET);
        return provider;
    }

    private ISubnetProvider checkAndGetProvider(Integer providerType) {
        ISubnetProvider provider = ResourceProviders.checkAndGet(ResourceType.SUBNET, providerType);
        return provider;
    }
}
