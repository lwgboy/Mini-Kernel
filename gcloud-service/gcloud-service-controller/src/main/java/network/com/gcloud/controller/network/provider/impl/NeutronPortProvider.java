package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.controller.network.dao.IpallocationDao;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.dao.SecurityGroupPortBindingDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.Ipallocation;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.entity.SecurityGroupPortBinding;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.enums.OvsBridgeRefType;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.provider.enums.neutron.NeutronDeviceOwner;
import com.gcloud.controller.network.service.IOvsBridgeService;
import com.gcloud.controller.network.service.IPortService;
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
import com.gcloud.service.common.compute.uitls.VmUtil;
import org.openstack4j.api.Builders;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.builder.PortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class NeutronPortProvider implements IPortProvider {

    @Autowired
    private NeutronProviderProxy neutronProviderProxy;

    @Autowired
    private IPortService portService;

    @Autowired
    private PortDao portDao;

    @Autowired
    private SecurityGroupDao securitygroupDao;

    @Autowired
    private SecurityGroupPortBindingDao securityGroupPortBindingDao;

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Autowired
    private IOvsBridgeService ovsBridgeService;

    @Autowired
    private NetworkDao networkDao;

    @Autowired
    private IpallocationDao ipallocationDao;

    @Autowired
    private SubnetDao subnetDao;

    @Override
    public ResourceType resourceType() {
        return ResourceType.PORT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    public final String BINDING_FAILED = "binding_failed";

    @Override
    public String createPort(CreatePortParams params, CurrentUser currentUser) {
        SimpleFlowChain<Port, String> chain = new SimpleFlowChain<>("create port");
        String portId = UUID.randomUUID().toString();
        chain.then(new Flow<Port>("create neutron port") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Port data) {
                org.openstack4j.model.network.Port port = neutronProviderProxy.createPort(params.getName(), params.getSecurityGroupRefId(), params.getSubnetRefId(),
                        params.getIpAddress());
                chain.data(port);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.Port data) {
                neutronProviderProxy.deletePort(data.getId());
            }

        }).then(new NoRollbackFlow<Port>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.Port data) {

                String ipAddress = params.getIpAddress();
                if(StringUtils.isBlank(ipAddress)){
                    Set<? extends IP> ips = data.getFixedIps();
                    if(ips != null && ips.size() > 0){
                        for(IP ip : ips){
                            ipAddress = ip.getIpAddress();
                            break;
                        }
                    }
                }

                //TODO
                //转state
                portService.createPortData(params, providerType(), currentUser, portId, 
                		data.getId(), data.getMacAddress(),
                		ResourceStates.status(ResourceType.PORT, ProviderType.NEUTRON, data.getState().name()), ipAddress);
                chain.next();
            }
        }).done(new FlowDoneHandler<Port>() {
            @Override
            public void handle(org.openstack4j.model.network.Port data) {
                chain.setResult(data.getId());
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }

        return portId;
    }

    @Override
    public void deletePort(com.gcloud.controller.network.entity.Port port) {
        portService.cleanPortData(port.getId());
        neutronProviderProxy.deletePort(port.getProviderRefId());
    }

    @Override
    public void updatePort(String portId, List<String> securityGroupIds, String portName) {
        List<String> updateField = new ArrayList<>();

        PortBuilder builder = Builders.port();
        if (securityGroupIds != null && securityGroupIds.size() > 0) {
            securityGroupPortBindingDao.deleteByPortId(portId);

            for (String sgId : securityGroupIds) {
                SecurityGroup securityGroup = securitygroupDao.getById(sgId);
                if (securityGroup == null) {
                    throw new GCloudException("0080203::找不到对应的安全组");
                }
                builder.securityGroup(sgId);

                SecurityGroupPortBinding bind = new SecurityGroupPortBinding();
                bind.setPortId(portId);
                bind.setSecurityGroupId(sgId);
                securityGroupPortBindingDao.save(bind);
            }

        }

        com.gcloud.controller.network.entity.Port port = new com.gcloud.controller.network.entity.Port();
        port.setId(portId);

        if (portName != null) {
            builder.name(portName);
            updateField.add(port.updateName(portName));
        }

        if (updateField.size() > 0) {
            portDao.update(port, updateField);
        }

        org.openstack4j.model.network.Port updatePort = builder.build();
        updatePort.setId(portId);

        neutronProviderProxy.updatePort(updatePort);
    }

    @Override
    public void attachPort(VmInstance instance, com.gcloud.controller.network.entity.Port port, String ovsBridgeId, Boolean noArpLimit) {

        String sufId = VmControllerUtil.generateNetcardSufId();
        String instanceId = instance.getId();

        if (StringUtils.isNotBlank(ovsBridgeId)) {
            OvsBridge ovsBridge = ovsBridgeDao.getById(ovsBridgeId);
            if (ovsBridge == null) {
                throw new GCloudException("::OVS 网桥不存在");
            }
        }

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("attach port");
        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                int res = portDao.attachPort(port.getId(), instanceId, DeviceOwner.COMPUTE.getValue(), sufId, VmUtil.getPreName(sufId), VmUtil.getAftName(sufId), VmUtil.getBrName(sufId),
                        ovsBridgeId, noArpLimit);
                if (res <= 0) {
                    throw new GCloudException("0010607::此网卡已经挂载，不能再挂载");
                }
                chain.next();

            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if(StringUtils.isNotBlank(ovsBridgeId)){
                    ovsBridgeService.allocate(ovsBridgeId, OvsBridgeRefType.PORT, port.getId());
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                if (StringUtils.isNotBlank(ovsBridgeId)) {
                    ovsBridgeService.release(ovsBridgeId, OvsBridgeRefType.PORT, port.getId());
                }
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                org.openstack4j.model.network.Port neutronPort = Builders.port().deviceId(instanceId).deviceOwner(DeviceOwner.COMPUTE.getValue()).hostId(instance.getHostname()).build();
                neutronPort.setId(port.getProviderRefId());
                neutronProviderProxy.updatePort(neutronPort);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                org.openstack4j.model.network.Port neutronPort = Builders.port().deviceId("").deviceOwner("").hostId("").build();
                neutronPort.setId(port.getProviderRefId());
                neutronProviderProxy.updatePort(neutronPort);
            }
        }).then(new NoRollbackFlow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                org.openstack4j.model.network.Port newPort = neutronProviderProxy.getPort(port.getProviderRefId());
                if (newPort == null || (newPort.getVifType() != null && BINDING_FAILED.equals(newPort.getVifType()))) {
                    throw new GCloudException("0010608::网卡挂载失败");
                }
            }
        }).start();

        if (StringUtils.isNotBlank(chain.getErrorCode())) {
            throw new GCloudException(chain.getErrorCode());
        }
    }

    @Override
    public void updatePort(com.gcloud.controller.network.entity.Port port) {

        List<String> updateField = new ArrayList<>();
        boolean updateNeutron = false;
        PortBuilder builder = Builders.port();

        com.gcloud.controller.network.entity.Port dbPort = new com.gcloud.controller.network.entity.Port();
        dbPort.setId(port.getId());

        //手动设置成null，防止Port有默认赋值
        if (port.getDescription() != null) {
            if (port.getDescription().equals("")) {
                dbPort.setDescription(null);
            }
            else {
                dbPort.setDescription(port.getDescription());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.DESCRIPTION);
        }

        if (port.getSufId() != null) {
            if (port.getSufId().equals("")) {
                dbPort.setSufId(null);
            }
            else {
                dbPort.setSufId(port.getSufId());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.SUF_ID);
        }

        if (port.getAftName() != null) {
            if (port.getAftName().equals("")) {
                dbPort.setAftName(null);
            }
            else {
                dbPort.setAftName(port.getAftName());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.AFT_NAME);
        }

        if (port.getBrName() != null) {
            if (port.getBrName().equals("")) {
                dbPort.setBrName(null);
            }
            else {
                dbPort.setBrName(port.getBrName());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.BR_NAME);
        }

        if (port.getPreName() != null) {
            if (port.getPreName().equals("")) {
                dbPort.setPreName(null);
            }
            else {
                dbPort.setPreName(port.getPreName());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.PRE_NAME);
        }

        if (port.getName() != null) {
            if (port.getName().equals("")) {
                dbPort.setName(null);
            }
            else {
                dbPort.setName(port.getName());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.NAME);
            builder.name(port.getName());
            updateNeutron = true;
        }

        if (port.getDeviceId() != null) {
            if (port.getDeviceId().equals("")) {
                dbPort.setDeviceId(null);
            }
            else {
                dbPort.setDeviceId(port.getDeviceId());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.DEVICE_ID);
            builder.deviceId(port.getDeviceId());
            updateNeutron = true;
        }

        if (port.getDeviceOwner() != null) {
            if (port.getDeviceOwner().equals("")) {
                dbPort.setDeviceOwner(null);
            }
            else {
                dbPort.setDeviceOwner(port.getDeviceOwner());
            }
            updateField.add(com.gcloud.controller.network.entity.Port.DEVICE_OWNER);
            builder.deviceOwner(port.getDeviceOwner());
            updateNeutron = true;
        }

        if (updateField.size() > 0) {
            portDao.update(dbPort, updateField);
        }

        if (updateNeutron) {
            org.openstack4j.model.network.Port updatePort = builder.build();
            updatePort.setId(port.getProviderRefId());
            neutronProviderProxy.updatePort(updatePort);
        }
    }

    @Override
    public void deleteQosPolicy(String policyId) {
        neutronProviderProxy.deleteQosPolicy(policyId);
    }

    @Override
    public List<com.gcloud.controller.network.entity.Port> list(Map<String, String> filter) {
        List<Port> ports = neutronProviderProxy.listPort(filter);
        List<com.gcloud.controller.network.entity.Port> retList = new ArrayList<>();
        for (Port p : ports) {
            com.gcloud.controller.network.entity.Port port = new com.gcloud.controller.network.entity.Port();
            port.setUpdatedAt(p.getUpdatedAt());
            port.setDeviceId(p.getDeviceId());
            port.setDeviceOwner(p.getDeviceOwner());
//            port.setCreateTime();
            port.setProvider(providerType().getValue());
            port.setProviderRefId(p.getId());
            port.setName(p.getName());
            port.setMacAddress(p.getMacAddress());
            port.setNetworkId(p.getNetworkId());
            port.setStatus(ResourceStates.status(ResourceType.PORT, ProviderType.NEUTRON, p.getState().name()));
            // TODO: other items.

            retList.add(port);
        }

        return retList;
    }

    @Override
    public void detachDone(com.gcloud.controller.network.entity.Port port) {

        org.openstack4j.model.network.Port updatePort = Builders.port().hostId("").deviceId("").deviceOwner("").build();
        updatePort.setId(port.getProviderRefId());
        neutronProviderProxy.updatePort(updatePort);

    }

    @Transactional
    public com.gcloud.controller.network.entity.Port createPortData(String neutronPortId, String deviceId, CurrentUser currentUser){

        org.openstack4j.model.network.Port neutronPort = neutronProviderProxy.getPort(neutronPortId);
        com.gcloud.controller.network.entity.Port port = null;
        if(neutronPort != null){
            String portId = UUID.randomUUID().toString();

            Map<String, Object> networkFilter = new HashMap<>();
            networkFilter.put(Subnet.PROVIDER, ProviderType.NEUTRON.getValue());
            networkFilter.put(Subnet.PROVIDER_REF_ID, neutronPort.getNetworkId());
            Network network = networkDao.findUniqueByProperties(networkFilter);

            port = new com.gcloud.controller.network.entity.Port();
            port.setId(portId);
            port.setMacAddress(neutronPort.getMacAddress());
            port.setNetworkId(network.getId());
            port.setUserId(currentUser.getId());
            port.setTenantId(currentUser.getDefaultTenant());

            port.setStatus(ResourceStates.status(ResourceType.PORT, ProviderType.NEUTRON, neutronPort.getState().name()));
            port.setName(neutronPort.getName());
            port.setCreateTime(new Date());
            port.setProvider(ProviderType.NEUTRON.getValue());
            port.setProviderRefId(neutronPort.getId());
            port.setDeviceOwner(NeutronDeviceOwner.toGCloudValue(neutronPort.getDeviceOwner()));

            String portDeviceId = StringUtils.isNotBlank(deviceId) ? deviceId : neutronPort.getDeviceId();
            port.setDeviceId(portDeviceId);

            portDao.save(port);

            //同一个provider，id唯一，可以以id作为key
            Map<String, Subnet> gcSubnetMap = new HashMap<>();

            if(neutronPort.getFixedIps() != null && neutronPort.getFixedIps().size() > 0){
                for(IP fixIp : neutronPort.getFixedIps()){

                    Subnet subnet = gcSubnetMap.get(fixIp.getSubnetId());
                    if(subnet == null){
                        Map<String, Object> subnetFilter = new HashMap<>();
                        subnetFilter.put(Subnet.PROVIDER, ProviderType.NEUTRON.getValue());
                        subnetFilter.put(Subnet.PROVIDER_REF_ID, fixIp.getSubnetId());
                        subnet = subnetDao.findUniqueByProperties(subnetFilter);
                    }

                    if(subnet == null){
                        throw new GCloudException("0080108::找不到对应的子网");
                    }

                    Ipallocation ipallocation = new Ipallocation();
                    ipallocation.setIpAddress(fixIp.getIpAddress());
                    ipallocation.setPortId(port.getId());
                    ipallocation.setSubnetId(subnet.getId());
                    ipallocation.setNetworkId(port.getNetworkId());
                    ipallocationDao.save(ipallocation);
                }
            }

        }

        return port;
    }
}
