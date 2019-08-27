package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.dao.QosBandwidthLimitRuleDao;
import com.gcloud.controller.network.dao.QosPolicyDao;
import com.gcloud.controller.network.entity.QosBandwidthLimitRule;
import com.gcloud.controller.network.entity.QosPolicy;
import com.gcloud.controller.network.service.IQosBandwidthLimitRuleService;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import org.openstack4j.model.network.QosDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/10/30.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class QosBandwidthLimitRuleServiceNeutronImpl implements IQosBandwidthLimitRuleService {

    @Autowired
    private NeutronProviderProxy neutronProviderProxy;

    @Autowired
    private QosBandwidthLimitRuleDao qosBandwidthLimitRuleDao;

    @Autowired
    private QosPolicyDao qosPolicyDao;

    @Override
    public String create(String policyId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection) {

        QosPolicy qosPolicy = qosPolicyDao.getById(policyId);
        if(qosPolicy == null){
            throw new GCloudException("::qos policy 不存在");
        }
        return createQosBandwidthLimitRule(policyId, maxKbps, maxBurstKbps, qosDirection);
    }

    @Override
    public String createQosBandwidthLimitRule(String policyId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection) {

        SimpleFlowChain<org.openstack4j.model.network.QosBandwidthLimitRule, String> chain = new SimpleFlowChain<>();
        chain.then(new Flow<org.openstack4j.model.network.QosBandwidthLimitRule>("create neutron qos bandwidth limit rule") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.QosBandwidthLimitRule data) {
                org.openstack4j.model.network.QosBandwidthLimitRule rule = neutronProviderProxy.createQosBandwidthLimitRule(policyId, maxKbps, maxBurstKbps, qosDirection);
                chain.data(rule);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.QosBandwidthLimitRule data) {
                neutronProviderProxy.deleteQosBandwidthLimitRule(policyId, data.getId());
            }
        }).then(new NoRollbackFlow<org.openstack4j.model.network.QosBandwidthLimitRule>("save qos bandwidth limit rule to db") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.QosBandwidthLimitRule data) {
                QosBandwidthLimitRule rule = new QosBandwidthLimitRule();
                rule.setId(data.getId());
                rule.setMaxKbps(maxKbps);
                rule.setMaxBurstKbps(maxBurstKbps);
                rule.setDirection(qosDirection.toJson());
                rule.setQosPolicyId(policyId);

                qosBandwidthLimitRuleDao.save(rule);

                chain.next();
            }
        }).done(new FlowDoneHandler<org.openstack4j.model.network.QosBandwidthLimitRule>() {
            @Override
            public void handle(org.openstack4j.model.network.QosBandwidthLimitRule data) {
                chain.setResult(data.getId());
            }
        }).start();

        if(StringUtils.isNotBlank(chain.getErrorCode())){
            throw new GCloudException(chain.getErrorCode());
        }

        return chain.getResult();
    }

    @Override
    public void update(String policyId, String ruleId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection) {

        QosBandwidthLimitRule rule = qosBandwidthLimitRuleDao.getById(ruleId);
        if(rule == null){
            throw new GCloudException("::");
        }
        updateQosBandwidthLimitRule(policyId, ruleId, maxKbps, maxBurstKbps, qosDirection);
    }

    @Override
    public void updateQosBandwidthLimitRule(String policyId, String ruleId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection) {

        List<String> updateField = new ArrayList<>();
        QosBandwidthLimitRule updateRule = new QosBandwidthLimitRule();
        updateRule.setId(ruleId);

        if(maxKbps != null){
            updateRule.setMaxKbps(maxKbps);
            updateField.add("maxKbps");
        }

        if(maxBurstKbps != null){
            updateRule.setMaxBurstKbps(maxBurstKbps);
            updateField.add("maxBurstKbps");
        }

        if(qosDirection != null){
            updateRule.setDirection(qosDirection.toJson());
            updateField.add("direction");
        }
        qosBandwidthLimitRuleDao.update(updateRule, updateField);

        neutronProviderProxy.updateQosBandwidthLimitRule(policyId, ruleId, maxKbps, maxBurstKbps, qosDirection);


    }

    @Override
    public void deleteQosBandwidthLimitRule(String policyId, String ruleId) {

        qosBandwidthLimitRuleDao.deleteById(ruleId);
        neutronProviderProxy.deleteQosBandwidthLimitRule(policyId, ruleId);

    }
}
