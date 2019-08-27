package com.gcloud.controller.network.provider.impl;

import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GcloudPortProvider implements IPortProvider {

    @Override
    public ResourceType resourceType() {
        return ResourceType.PORT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    @Override
    public String createPort(CreatePortParams params, CurrentUser currentUser) {
        return null;
    }

    @Override
    public void deletePort(Port portId) {

    }

    @Override
    public void updatePort(String portId, List<String> securityGroupIds, String portName) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attachPort(VmInstance instance, Port port, String customOvsBr, Boolean noArpLimit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePort(Port port) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteQosPolicy(String policyId) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Port> list(Map<String, String> filter) {
        //
        return null;
    }

    @Override
    public void detachDone(Port port) {

    }

    @Override
    public Port createPortData(String neutronPortId, String deviceId, CurrentUser currentUser) {
        return null;
    }
}
