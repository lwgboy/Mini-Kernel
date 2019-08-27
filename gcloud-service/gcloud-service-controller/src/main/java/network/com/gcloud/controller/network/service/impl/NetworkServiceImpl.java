package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.enums.NetworkType;
import com.gcloud.controller.network.model.CreateNetworkParams;
import com.gcloud.controller.network.provider.INetworkProvider;
import com.gcloud.controller.network.service.INetworkService;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.ExternalNetworkSetType;
import com.gcloud.header.network.model.VSwitchIds;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.CreateExternalNetworkMsg;
import com.gcloud.header.network.msg.api.DescribeExternalNetworksMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;
import com.gcloud.header.network.msg.api.ModifyVpcAttributeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class NetworkServiceImpl implements INetworkService {
    @Autowired
    private NetworkDao networkDao;

    @Autowired
    private IPortService portService;

    @Autowired
    private SubnetDao subnetDao;

    @Autowired
    private PortDao portDao;

    public PageResult<VpcsItemType> describeVpcs(DescribeVpcsMsg msg) {
        return networkDao.describeVpcs(msg);
    }

    @Override
    public String createNetwork(CreateNetworkParams params, CurrentUser currentUser) {
        String networkId = StringUtils.genUuid();
        this.getProviderOrDefault().createNetwork(networkId, params, currentUser);
        return networkId;
    }
    
    @Override
    public String createExternalNetwork(CreateExternalNetworkMsg msg) {
        String networkId = StringUtils.genUuid();
        this.getProviderOrDefault().createExternalNetwork(networkId, msg);
        return networkId;
    }

    @Override
    public void removeNetwork(String networkId) {
        Network network = networkDao.getById(networkId);
        if(network == null) {
        	throw new GCloudException("0160202::网络不存在");
        }

        if(subnetDao.hasSubnet(networkId)){
            throw new GCloudException("0160203::有关联的子网，不能删除");
        }

        List<Port> ports = portDao.findByProperty(Port.NETWORK_ID, networkId);
        if(ports != null && ports.size() > 0){
            for(Port port : ports){
                if(DeviceOwner.GATEWAY.getValue().equals(port.getDeviceOwner())){
                    throw new GCloudException("0160204::有关联的路由网关，不能删除");
                }else{
                    throw new GCloudException("0160205::有关联的端口，不能删除");
                }
            }
        }

    	networkDao.deleteById(networkId);
        this.checkAndGetProvider(network.getProvider()).removeNetwork(network.getProviderRefId());
    }

    @Override
    public void updateNetwork(ModifyVpcAttributeMsg msg) {
        Network network = networkDao.getById(msg.getVpcId());
        if (network == null) {
            throw new GCloudException("0160203::网络不存在");
        }
        List<String> updateField = new ArrayList<String>();
        updateField.add(network.updateName(msg.getVpcName()));
        updateField.add(network.updateUpdatedAt(new Date()));
        networkDao.update(network, updateField);
        CacheContainer.getInstance().put(CacheType.NETWORK_NAME, msg.getVpcId(), msg.getVpcName());
       
        this.checkAndGetProvider(network.getProvider()).updateNetwork(network.getProviderRefId(), msg.getVpcName());
    
    }
    
    public PageResult<ExternalNetworkSetType> describeNetworks(DescribeExternalNetworksMsg msg) {
    	//查询外部网络
    	msg.setType(NetworkType.EXTERNAL.getValue());
    	PageResult<ExternalNetworkSetType> result = networkDao.describeNetworks(msg);	
    	for(ExternalNetworkSetType network: result.getList()) {
    		if(network.getSubnetIds() != null) {
    			String subnetIdsStr = network.getSubnetIds();
    			List<String> subnetIds = Arrays.asList(subnetIdsStr.split(","));
    			VSwitchIds vSwitchIds = new VSwitchIds();
    			vSwitchIds.setvSwitchId(subnetIds);
    			network.setvSwitchIds(vSwitchIds);
    		}	
    	}
        return result;
    }

    @Override
    public void getNetworks(String id) {
        // TODO Auto-generated method stub

    }

    private INetworkProvider getProviderOrDefault() {
        INetworkProvider provider = ResourceProviders.getDefault(ResourceType.NETWORK);
        return provider;
    }

    private INetworkProvider checkAndGetProvider(Integer providerType) {
        INetworkProvider provider = ResourceProviders.checkAndGet(ResourceType.NETWORK, providerType);
        return provider;
    }

}
