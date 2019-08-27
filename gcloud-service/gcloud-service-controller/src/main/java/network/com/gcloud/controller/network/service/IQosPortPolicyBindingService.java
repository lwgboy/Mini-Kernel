package com.gcloud.controller.network.service;

/**
 * Created by yaowj on 2018/10/30.
 */
public interface IQosPortPolicyBindingService {
    void bind(String portId, String policyId);
    void bindPortAndQosPolicy(String portId, String policyId);

}
