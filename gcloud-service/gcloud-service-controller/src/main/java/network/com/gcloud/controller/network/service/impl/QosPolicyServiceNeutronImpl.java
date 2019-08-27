package com.gcloud.controller.network.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.dao.QosBandwidthLimitRuleDao;
import com.gcloud.controller.network.dao.QosPolicyDao;
import com.gcloud.controller.network.dao.QosPortPolicyBindingDao;
import com.gcloud.controller.network.entity.QosPolicy;
import com.gcloud.controller.network.service.IQosPolicyService;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import org.openstack4j.api.Builders;
import org.openstack4j.model.network.builder.QosPolicyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yaowj on 2018/10/30.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class QosPolicyServiceNeutronImpl implements IQosPolicyService {

    @Autowired
    private NeutronProviderProxy neutronProviderProxy;

    @Autowired
    private QosPolicyDao qosPolicyDao;

    @Autowired
    private QosBandwidthLimitRuleDao qosBandwidthLimitRuleDao;

    @Autowired
    private QosPortPolicyBindingDao qosPortPolicyBindingDao;

    @Override
    public String create(String name, String description, Boolean isDefault, Boolean shared) {
        return createQosPolicy(name, description, isDefault, shared);
    }

    @Override
    public String createQosPolicy(String name, String description, Boolean isDefault, Boolean shared) {

        SimpleFlowChain<org.openstack4j.model.network.QosPolicy, String> chain = new SimpleFlowChain<>("create qos policy");
        chain.then(new NoRollbackFlow<org.openstack4j.model.network.QosPolicy>("create neutron qos policy") {
            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.QosPolicy data) {
                QosPolicyBuilder qosPolicyBuilder = Builders.qosPolicy();
                if(name != null){
                    qosPolicyBuilder.name(name);
                }
                if(description != null){
                    qosPolicyBuilder.description(description);
                }
                if(isDefault != null){
                    qosPolicyBuilder.isDefault(isDefault);
                }
                if(shared != null){
                    qosPolicyBuilder.shared(shared);
                }
                org.openstack4j.model.network.QosPolicy policy = neutronProviderProxy.createQosPolicy(qosPolicyBuilder.build());
                chain.data(policy);
                chain.next();

            }
            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.network.QosPolicy data) {
                neutronProviderProxy.deleteQosPolicy(data.getId());
            }
        }).then(new NoRollbackFlow<org.openstack4j.model.network.QosPolicy>("save qos policy to db") {

            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.network.QosPolicy data) {
                QosPolicy policy = new QosPolicy();
                policy.setId(data.getId());
                qosPolicyDao.save(policy);
                chain.next();
            }


        }).done(new FlowDoneHandler<org.openstack4j.model.network.QosPolicy>() {
            @Override
            public void handle(org.openstack4j.model.network.QosPolicy data) {
                chain.setResult(data.getId());
            }
        }).start();

        if(StringUtils.isNotBlank(chain.getErrorCode())){
            throw new GCloudException(chain.getErrorCode());
        }

        return chain.getResult();
    }

    public void deleteQosPolicy(String id){

        //TODO 如果增肌其他rule，要增加删除
        qosBandwidthLimitRuleDao.deleteByPolicyId(id);

        qosPortPolicyBindingDao.deleteByPolicyId(id);

        qosPolicyDao.deleteById(id);

        neutronProviderProxy.deleteQosPolicy(id);

    }
}
