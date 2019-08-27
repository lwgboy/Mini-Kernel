package com.gcloud.controller.network.service;

import org.openstack4j.model.network.QosDirection;

/**
 * Created by yaowj on 2018/10/30.
 */
public interface IQosBandwidthLimitRuleService {

    //带有参数校验
    String create(String policyId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection);
    //不带参数校验，在调用层校验
    String createQosBandwidthLimitRule(String policyId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection);

    void update(String policyId, String ruleId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection);
    void updateQosBandwidthLimitRule(String policyId, String ruleId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection);

    void deleteQosBandwidthLimitRule(String policyId, String ruleId);
}
