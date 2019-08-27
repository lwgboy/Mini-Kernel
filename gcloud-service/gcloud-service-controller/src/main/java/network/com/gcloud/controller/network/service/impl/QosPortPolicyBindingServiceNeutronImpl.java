package com.gcloud.controller.network.service.impl;

import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.QosPolicyDao;
import com.gcloud.controller.network.dao.QosPortPolicyBindingDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.QosPolicy;
import com.gcloud.controller.network.entity.QosPortPolicyBinding;
import com.gcloud.controller.network.service.IQosPortPolicyBindingService;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import org.openstack4j.api.Builders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yaowj on 2018/10/30.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class QosPortPolicyBindingServiceNeutronImpl implements IQosPortPolicyBindingService {

    @Autowired
    private PortDao portDao;

    @Autowired
    private QosPolicyDao qosPolicyDao;

    @Autowired
    private QosPortPolicyBindingDao qosPortPolicyBindingDao;

    @Autowired
    private NeutronProviderProxy neutronProviderProxy;

    @Override
    public void bind(String portId, String policyId) {

        Port port = portDao.getById(portId);
        if(port == null){
            throw new GCloudException("::");
        }

        QosPolicy qosPolicy = qosPolicyDao.getById(policyId);
        if(qosPolicy == null){
            throw new GCloudException("::");
        }

        bindPortAndQosPolicy(portId, policyId);
    }

    @Override
    public void bindPortAndQosPolicy(String portId, String policyId) {

        QosPortPolicyBinding bind = new QosPortPolicyBinding();
        bind.setPolicyId(policyId);
        bind.setPortId(portId);
        qosPortPolicyBindingDao.save(bind);

        org.openstack4j.model.network.Port updatePort = Builders.port().qosPolicyId(policyId).build();
        updatePort.setId(portId);
        neutronProviderProxy.updatePort(updatePort);

    }
}
