package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.QosBandwidthLimitRuleDao;
import com.gcloud.controller.network.dao.QosPortPolicyBindingDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.QosBandwidthLimitRule;
import com.gcloud.controller.network.entity.QosPortPolicyBinding;
import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.controller.network.model.AssociateEipAddressParams;
import com.gcloud.controller.network.model.ModifyEipAddressAttributeParams;
import com.gcloud.controller.network.provider.IFloatingIpProvider;
import com.gcloud.controller.network.service.IFloatingIpService;
import com.gcloud.controller.network.service.IQosBandwidthLimitRuleService;
import com.gcloud.controller.network.service.IQosPolicyService;
import com.gcloud.controller.network.service.IQosPortPolicyBindingService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.EipAddressSetType;
import com.gcloud.header.network.msg.api.DescribeEipAddressesMsg;
import org.openstack4j.model.network.QosDirection;
import org.openstack4j.model.network.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class FloatingIpServiceImpl implements IFloatingIpService {
    @Autowired
    FloatingIpDao floatingipDao;

    @Autowired
    PortDao portDao;

    @Autowired
    InstanceDao vmInstanceDao;
    
    @Autowired
    private IQosPolicyService qosPolicyService;

    @Autowired
    private IQosBandwidthLimitRuleService qosBandwidthLimitRuleService;
    
    @Autowired
    private IQosPortPolicyBindingService qosPortPolicyBindingService;
    
    @Autowired
    private QosPortPolicyBindingDao qosPortPolicyBindingDao;

    @Autowired
    private QosBandwidthLimitRuleDao qosBandwidthLimitRuleDao;

    @Override
    public PageResult<EipAddressSetType> describeEipAddresses(DescribeEipAddressesMsg param) {
        return floatingipDao.getByPage(param);
    }

    @Override
    public AllocateEipAddressResponse allocateEipAddress(String networkId, String regionId, CurrentUser currentUser) {
        return this.getProviderOrDefault().allocateEipAddress(networkId, regionId, currentUser);
    }

    @Override
    public void associateEipAddress(AssociateEipAddressParams params) {
        FloatingIp floatingip = floatingipDao.getById(params.getAllocationId());
        if (floatingip == null) {
            throw new GCloudException("0050204::弹性公网ip不存在");
        }
        Port netcard = portDao.getById(params.getNetcardId());
        if (netcard == null) {
            throw new GCloudException("0050205::网卡不存在");
        }
        VmInstance instance = vmInstanceDao.getById(params.getInstanceId());
        if (instance == null) {
            throw new GCloudException("0050206::云服务器不存在");
        }
        this.checkAndGetProvider(floatingip.getProvider()).associateEipAddress(params.getAllocationId(), params.getNetcardId());
        // 更新eip表
        floatingip.setFixedPortId(params.getNetcardId());
        floatingip.setInstanceId(params.getInstanceId());
        floatingip.setStatus(ResourceStates.status(ResourceType.FLOATING_IP, ProviderType.NEUTRON, State.ACTIVE.toString()));
        floatingipDao.update(floatingip);
    }

    @Override
    public void unAssociateEipAddress(String allocationId) {
        FloatingIp floatingip = floatingipDao.getById(allocationId);
        if (floatingip == null) {
            throw new GCloudException("0050302::弹性公网ip不存在");
        }
        // 更新eip表
        List<String> updatedField = new ArrayList<String>();
        updatedField.add(floatingip.updateFixedPortId(null));
        updatedField.add(floatingip.updateInstanceId(null));
        updatedField.add(floatingip.updateStatus(ResourceStates.status(ResourceType.FLOATING_IP, ProviderType.NEUTRON, State.DOWN.toString())));
        
        floatingipDao.update(floatingip, updatedField);
        this.checkAndGetProvider(floatingip.getProvider()).unAssociateEipAddress(floatingip.getProviderRefId());
    }

    @Override
    public void releaseEipAddress(String allocationId) {
        FloatingIp floatingip = floatingipDao.getById(allocationId);
        if (floatingip == null) {
            throw new GCloudException("0050402::弹性公网ip不存在");
        }
     // 更新eip表
        floatingipDao.deleteById(allocationId);
        Port port = portDao.findUniqueByProperty("device_id", allocationId);
        if(port == null) {
        	throw new GCloudException("0050403::port不存在");
        }
        portDao.deleteById(port.getId());
        this.checkAndGetProvider(floatingip.getProvider()).releaseEipAddress(floatingip.getProviderRefId());
        
    }

    @Override
    public void modifyEipAddressAttribute(ModifyEipAddressAttributeParams param) {
        FloatingIp floatingip = floatingipDao.getById(param.getAllocationId());
        if (floatingip == null) {
            throw new GCloudException("::弹性公网ip不存在");
        }
        QosPortPolicyBinding binding = qosPortPolicyBindingDao.findUniqueByProperty("port_id", floatingip.getFloatingPortId());
        if (binding == null) {
            String policyId = qosPolicyService.createQosPolicy("弹性公网带宽限制", null, null, null);
            qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, param.getBandwidth() * 1000, null, QosDirection.EGRESS);
            qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, param.getBandwidth() * 1000, null, QosDirection.INGRESS);
            if (StringUtils.isNotBlank(floatingip.getFloatingPortId())) {
                qosPortPolicyBindingService.bindPortAndQosPolicy(floatingip.getFloatingPortId(), policyId);
            }
        }
        else {
            String policyId = binding.getPolicyId();
            List<QosBandwidthLimitRule> bandwidthLimitRuleList = qosBandwidthLimitRuleDao.findByProperty("qosPolicyId", binding.getPolicyId());
            String ingressRuleId = null;
            String egressRuleId = null;
            if (bandwidthLimitRuleList != null && bandwidthLimitRuleList.size() > 0) {
                for (QosBandwidthLimitRule rule : bandwidthLimitRuleList) {
                    if (QosDirection.EGRESS.toJson().equals(rule.getDirection())) {
                        egressRuleId = rule.getId();
                    }
                    else if (QosDirection.INGRESS.toJson().equals(rule.getDirection())) {
                        ingressRuleId = rule.getId();
                    }
                }
            }

            if (StringUtils.isBlank(egressRuleId)) {
                qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, param.getBandwidth() * 1000, null, QosDirection.EGRESS);
            }
            else {
                qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, egressRuleId, param.getBandwidth() * 1000, null, QosDirection.EGRESS);
            }

            if (StringUtils.isBlank(ingressRuleId)) {
                qosBandwidthLimitRuleService.createQosBandwidthLimitRule(policyId, param.getBandwidth() * 1000, null, QosDirection.INGRESS);
            }
            else {
                qosBandwidthLimitRuleService.updateQosBandwidthLimitRule(policyId, ingressRuleId, param.getBandwidth() * 1000, null, QosDirection.INGRESS);
            }
        }
    }

    private IFloatingIpProvider getProviderOrDefault() {
        IFloatingIpProvider provider = ResourceProviders.getDefault(ResourceType.FLOATING_IP);
        return provider;
    }

    private IFloatingIpProvider checkAndGetProvider(Integer providerType) {
        IFloatingIpProvider provider = ResourceProviders.checkAndGet(ResourceType.FLOATING_IP, providerType);
        return provider;
    }

}
