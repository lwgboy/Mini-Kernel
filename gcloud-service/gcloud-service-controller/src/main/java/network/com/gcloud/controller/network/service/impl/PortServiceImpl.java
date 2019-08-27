package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.dao.IpallocationDao;
import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.QosBandwidthLimitRuleDao;
import com.gcloud.controller.network.dao.QosPortPolicyBindingDao;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.dao.SecurityGroupPortBindingDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.controller.network.entity.Ipallocation;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.QosBandwidthLimitRule;
import com.gcloud.controller.network.entity.QosPortPolicyBinding;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.entity.SecurityGroupPortBinding;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.model.DescribeNetworkInterfacesParams;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.controller.network.service.IQosBandwidthLimitRuleService;
import com.gcloud.controller.network.service.IQosPolicyService;
import com.gcloud.controller.network.service.IQosPortPolicyBindingService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.NetworkInterfaceSet;
import com.gcloud.header.network.model.SecurityGroupIdSetType;
import org.apache.commons.lang.ObjectUtils;
import org.openstack4j.model.network.QosDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2018/12/7.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PortServiceImpl implements IPortService {

    @Autowired
    private PortDao portDao;

    @Autowired
    private IpallocationDao ipallocationDao;

    @Autowired
    private SubnetDao subnetDao;

    @Autowired
    private SecurityGroupDao securitygroupDao;

    @Autowired
    private SecurityGroupPortBindingDao securityGroupPortBindingDao;

    @Autowired
    private IQosPolicyService qosPolicyService;

    @Autowired
    private QosPortPolicyBindingDao qosPortPolicyBindingDao;

    @Autowired
    private QosBandwidthLimitRuleDao qosBandwidthLimitRuleDao;

    @Autowired
    private IQosBandwidthLimitRuleService qosBandwidthLimitRuleService;

    @Autowired
    private IQosPortPolicyBindingService qosPortPolicyBindingService;

    @Autowired
    private FloatingIpDao floatingIpDao;

    @Autowired
    private OvsBridgeDao ovsBridgeDao;


    @Override
    public PageResult<NetworkInterfaceSet> describe(DescribeNetworkInterfacesParams params, CurrentUser currentUser){

        if(params == null){
            params = new DescribeNetworkInterfacesParams();
        }

        PageResult<NetworkInterfaceSet> pageResult = portDao.describePorts(params, NetworkInterfaceSet.class, currentUser);
        List<NetworkInterfaceSet> datas = pageResult.getList();
        if(datas != null && datas.size() > 0){
            for(NetworkInterfaceSet data : datas){

                String sgIds = data.getSecurityGroupIdsStr();
                if(StringUtils.isNotBlank(sgIds)){
                    SecurityGroupIdSetType sgType = new SecurityGroupIdSetType();
                    sgType.setSecurityGroupId(Arrays.asList(sgIds.split(",")));
                    data.setSecurityGroupIds(sgType);
                }
            }
        }

        return pageResult;
    }

    @Override
    public VmNetworkDetail getNetworkDetail(String portId) {
        Port port = portDao.getById(portId);

        VmNetworkDetail networkDetail = new VmNetworkDetail();
        networkDetail.setSufId(port.getSufId());
        networkDetail.setAftName(port.getAftName());
        networkDetail.setPreName(port.getPreName());
        networkDetail.setBrName(port.getBrName());
        networkDetail.setMacAddress(port.getMacAddress());
        networkDetail.setDeviceOwner(port.getDeviceOwner());
        networkDetail.setPortId(port.getId());
        networkDetail.setDeviceOwner(DeviceOwner.COMPUTE.getValue());
        networkDetail.setNoArpLimit(port.getNoArpLimit());
        networkDetail.setPortRefId(port.getProviderRefId());
        networkDetail.setPortProvider(port.getProvider());

        //
        Ipallocation ipallocation = ipallocationDao.findUniqueByProperty("portId", port.getId());
        if(ipallocation != null){
            networkDetail.setIp(ipallocation.getIpAddress());
            networkDetail.setSubnetId(ipallocation.getSubnetId());
        }

        if(StringUtils.isNotBlank(port.getOvsBridgeId())){
            OvsBridge ovsBridge = ovsBridgeDao.getById(port.getOvsBridgeId());
            if(ovsBridge == null){
                throw new GCloudException("::获取ovs网桥失败");
            }
            networkDetail.setCustomOvsBr(ovsBridge.getBridge());
            networkDetail.setOvsBridgeId(ovsBridge.getId());
        }

        return networkDetail;
    }


    @Override
    public String create(CreatePortParams params, CurrentUser currentUser){

        Subnet subnet = subnetDao.getById(params.getSubnetId());
        if(subnet == null){
            throw new GCloudException("0080103::找不到对应的子网");
        }
        params.setSubnetRefId(subnet.getProviderRefId());
        params.setNetworkId(subnet.getNetworkId());

        if(StringUtils.isNotBlank(params.getSecurityGroupId())){
            SecurityGroup securitygroup = securitygroupDao.getById(params.getSecurityGroupId());
            if(securitygroup == null){
                throw new GCloudException("0080104::找不到对应的安全组");
            }
            params.setSecurityGroupRefId(securitygroup.getProviderRefId());
        }else {
            Map<String, Object> filters = new HashMap<>();
            filters.put(SecurityGroup.TENANT_ID, currentUser.getDefaultTenant());
            filters.put(SecurityGroup.IS_DEFAULT, true);
            SecurityGroup securitygroup = securitygroupDao.findUniqueByProperties(filters);
            if(securitygroup == null){
                throw new GCloudException("0080104::找不到对应的安全组");
            }
            params.setSecurityGroupId(securitygroup.getId());
            params.setSecurityGroupRefId(securitygroup.getProviderRefId());
        }


        return this.getProviderOrDefault(subnet.getProvider()).createPort(params, currentUser);
    }

    @Override
    public void update(String portId, List<String> securityGroupIds, String portName){

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0080202::找不到对应的端口");
        }

        updatePort(portId, securityGroupIds, portName);
        CacheContainer.getInstance().put(CacheType.PORT_NAME, portId, portName);
    }

    @Override
    public void updatePort(String portId, List<String> securityGroupIds, String portName){
        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0080302::找不到对应的端口");
        }
        this.checkAndGetProvider(port.getProvider()).updatePort(portId, securityGroupIds, portName);
    }

    @Override
    public void delete(String portId) {

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0080302::找不到对应的端口");
        }

        if(StringUtils.isNotBlank(port.getDeviceId())){
            throw new GCloudException("0080303::端口已经挂载，请卸载后再删除");
        }
        //改为，有绑定就不能删
        List<FloatingIp> fips = floatingIpDao.findByProperty(FloatingIp.FIXED_PORT_ID, portId);
        if(fips != null && fips.size() > 0){
            throw new GCloudException("0080304::请先卸载弹性公网ip");
        }
//        if(fips != null && fips.size() > 0){
//            fips.stream().forEach(fip -> floatingIpService.releaseEipAddress(fip.getId()));
//        }

        this.checkAndGetProvider(port.getProvider()).deletePort(port);

    }

    @Override
    public void updateQos(String portId, Integer egress, Integer ingress) {
        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0080502::找不到对应的端口");
        }

        updatePortQos(portId, egress, ingress);
    }

    @Override
    public void updatePortQos(String portId, Integer egress, Integer ingress){
        Port port = portDao.getById(portId);
        //是否已经有策略
        //neutron port 只支持绑定一个策略,如果升级neutron版本后，支持多个策略，则需要修改逻辑
        QosPortPolicyBinding binding = qosPortPolicyBindingDao.findUniqueByProperty("port_id", portId);
        if(binding == null){
            createQosLimit(port, egress, ingress);
        }else{
            updateQosLimit(binding.getPolicyId(), egress, ingress);
        }
    }

    private void createQosLimit(Port port, Integer egress, Integer ingress){

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("create qos limit");
        chain.then(new Flow<String>("create qos policy") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                String policyId = qosPolicyService.createQosPolicy("带宽限制", null, null, null);
                chain.data(policyId);
                chain.next();
            }
            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                checkAndGetProvider(port.getProvider()).deleteQosPolicy(data);
            }

            //创建规则的时候不会滚，直接又会policy回滚是删除
        }).then(new NoRollbackFlow<String>("create egress") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                qosBandwidthLimitRuleService.createQosBandwidthLimitRule(data, egress, null, QosDirection.EGRESS);
                chain.next();
            }

        }).then(new NoRollbackFlow<String>("create ingress") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                qosBandwidthLimitRuleService.createQosBandwidthLimitRule(data, ingress, null, QosDirection.INGRESS);
                chain.next();
            }
        }).then(new NoRollbackFlow<String>("bind port and qos policy") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                qosPortPolicyBindingService.bindPortAndQosPolicy(port.getId(), data);
                chain.next();
            }
        }).start();

        if(StringUtils.isNotBlank(chain.getErrorCode())){
            throw new GCloudException(chain.getErrorCode());
        }
    }

    private void updateQosLimit(String policyId, Integer egress, Integer ingress){
        List<QosBandwidthLimitRule> bandwidthLimitRuleList = qosBandwidthLimitRuleDao.findByProperty(QosBandwidthLimitRule.QOS_POLICY_ID, policyId);

        Map<String, Object> ruleMap = new HashMap<>();

        if(bandwidthLimitRuleList != null && bandwidthLimitRuleList.size() > 0){
            for(QosBandwidthLimitRule rule : bandwidthLimitRuleList){
                if(QosDirection.EGRESS.toJson().equals(rule.getDirection())){
                    ruleMap.put("egressRule", rule);
                }else if(QosDirection.INGRESS.toJson().equals(rule.getDirection())){
                    ruleMap.put("ingressRule", rule);
                }
            }
        }

        SimpleFlowChain<Map<String, Object>, String> chain = new SimpleFlowChain<>("update qos limit");
        chain.data(ruleMap).then(new Flow<Map<String, Object>>("update egress rule") {
            @Override
            public void run(SimpleFlowChain chain, Map<String, Object> data) {
                QosBandwidthLimitRule egressRule = data.get("egressRule") == null ? null : (QosBandwidthLimitRule)data.get("egressRule");
                if(egressRule == null){
                    String newEgressRuleId = qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, egress, null, QosDirection.EGRESS);
                    data.put("newEgressRuleId", newEgressRuleId);
                }else{
                    qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, egressRule.getId(), egress, null, QosDirection.EGRESS);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, Map<String, Object> data) {
                QosBandwidthLimitRule egressRule = data.get("egressRule") == null ? null : (QosBandwidthLimitRule)data.get("egressRule");
                if(egressRule == null){
                    String newEgressRuleId = ObjectUtils.toString(data.get("newEgressRuleId"));
                    qosBandwidthLimitRuleService.deleteQosBandwidthLimitRule(policyId, newEgressRuleId);
                }else{
                    qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, egressRule.getId(), egressRule.getMaxKbps(), null, QosDirection.EGRESS);
                }
            }
        }).then(new Flow<Map<String, Object>>("update egress rule") {
            @Override
            public void run(SimpleFlowChain chain, Map<String, Object> data) {
                QosBandwidthLimitRule ingressRule = data.get("ingressRule") == null ? null : (QosBandwidthLimitRule)data.get("ingressRule");
                if(ingressRule == null){
                    String newIngressRuleId = qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, ingress, null, QosDirection.INGRESS);
                    data.put("newIngressRuleId", newIngressRuleId);
                }else{
                    qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, ingressRule.getId(), ingress, null, QosDirection.INGRESS);
                }

                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, Map<String, Object> data) {
                QosBandwidthLimitRule ingressRule = data.get("ingressRule") == null ? null : (QosBandwidthLimitRule)data.get("ingressRule");
                if(ingressRule == null){
                    String newIngressRuleId = ObjectUtils.toString(data.get("newIngressRuleId"));
                    qosBandwidthLimitRuleService.deleteQosBandwidthLimitRule(policyId, newIngressRuleId);
                }else{
                    qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, ingressRule.getId(), ingressRule.getMaxKbps(), null, QosDirection.INGRESS);
                }
            }
        }).start();

        if(StringUtils.isNotBlank(chain.getErrorCode())){
            throw new GCloudException(chain.getErrorCode());
        }

    }


    @Override
    public void updatePort(Port port) {
        this.checkAndGetProvider(port.getProvider()).updatePort(port);
    }

    @Override
    public void attachPort(VmInstance instance, String portId, String customOvsBr, Boolean noArpLimit) {
        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("0080502::找不到对应的端口");
        }
        this.checkAndGetProvider(port.getProvider()).attachPort(instance, port, customOvsBr, noArpLimit);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cleanPortData(String portId) {
        ipallocationDao.deleteByPortId(portId);
        securityGroupPortBindingDao.deleteByPortId(portId);
        qosPortPolicyBindingDao.deleteByPortId(portId);
        portDao.deleteById(portId);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createPortData(CreatePortParams params, ProviderType provider, CurrentUser currentUser, String portId, String portRefId, String macAddress, String state, String ipAddress) {
        Port port = new Port();
        port.setId(portId);
        port.setMacAddress(macAddress);
        port.setNetworkId(params.getNetworkId());
        port.setUserId(currentUser.getId());
        port.setTenantId(currentUser.getDefaultTenant());
        port.setDescription(params.getDescription());
        port.setStatus(state);
        port.setName(params.getName());
        port.setCreateTime(new Date());
        port.setProvider(provider.getValue());
        port.setProviderRefId(portRefId);
        portDao.save(port);

        if(StringUtils.isNotBlank(ipAddress)){
            Ipallocation ipallocation = new Ipallocation();
            ipallocation.setIpAddress(ipAddress);
            ipallocation.setPortId(port.getId());
            ipallocation.setSubnetId(params.getSubnetId());
            ipallocation.setNetworkId(port.getNetworkId());
            ipallocationDao.save(ipallocation);
        }

        SecurityGroupPortBinding bind = new SecurityGroupPortBinding();
        bind.setPortId(port.getId());
        bind.setSecurityGroupId(params.getSecurityGroupId());

        securityGroupPortBindingDao.save(bind);
    }

    private IPortProvider getProviderOrDefault(Integer providerType) {
        IPortProvider provider = ResourceProviders.getOrDefault(ResourceType.PORT, providerType);
        return provider;
    }
    
    private IPortProvider checkAndGetProvider(Integer providerType) {
        IPortProvider provider = ResourceProviders.checkAndGet(ResourceType.PORT, providerType);
        return provider;
    }

    @Override
    public void detachDone(Port port) {
        Port updatePort = new Port();
        updatePort.setId(port.getId());
        List<String> updateField = new ArrayList<>();

        updateField.add(updatePort.updateSufId(""));
        updateField.add(updatePort.updateAftName(""));
        updateField.add(updatePort.updatePreName(""));
        updateField.add(updatePort.updateBrName(""));
        updateField.add(updatePort.updateDeviceOwner(""));
        updateField.add(updatePort.updateDeviceId(""));
        updateField.add(updatePort.updateOvsBridgeId(""));

        portDao.update(updatePort, updateField);
        checkAndGetProvider(port.getProvider()).detachDone(port);

    }
}
