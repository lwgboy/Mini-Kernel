package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.header.api.model.CurrentUser;

import java.util.List;
import java.util.Map;

public interface IPortProvider extends IResourceProvider {

    String createPort(CreatePortParams params, CurrentUser currentUser);

    void deletePort(Port port);

    void updatePort(String portId, List<String> securityGroupIds, String portName);

    void attachPort(VmInstance instance, Port port, String customOvsBr, Boolean noArpLimit);

    void updatePort(Port port);

    void deleteQosPolicy(String policyId);

    List<Port> list(Map<String, String> filter);

    void detachDone(Port port);

    Port createPortData(String neutronPortId, String deviceId, CurrentUser currentUser);



}
